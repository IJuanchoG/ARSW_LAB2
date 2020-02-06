package edu.eci.arsw.highlandersim;

import java.util.List;
import java.util.Random;

public class Immortal extends Thread {

    private ImmortalUpdateReportCallback updateCallback=null;
    
    private int health;
    
    private boolean estado, vivo;
    
    private int defaultDamageValue;

    private final List<Immortal> immortalsPopulation;

    private final String name;

    private final Random r = new Random(System.currentTimeMillis());
    
    private static Object sincronizador = ControlFrame.getSincronizador();
    
    private static Object desempatar = new Object();
    public Immortal(String name, List<Immortal> immortalsPopulation, int health, int defaultDamageValue, ImmortalUpdateReportCallback ucb) {
        super(name);
        this.updateCallback=ucb;
        this.name = name;
        this.immortalsPopulation = immortalsPopulation;
        this.health = health;
        this.defaultDamageValue=defaultDamageValue;
        this.estado = false;
        this.vivo = true;
    }

    public void run() {

        while (this.estaVivo()) {
            Immortal im;

            int myIndex = immortalsPopulation.indexOf(this);

            int nextFighterIndex = r.nextInt(immortalsPopulation.size());

            //avoid self-fight
            if (nextFighterIndex == myIndex) {
                nextFighterIndex = ((nextFighterIndex + 1) % immortalsPopulation.size());
            }

            im = immortalsPopulation.get(nextFighterIndex);
            
                this.sincronicePelea(im);
            
            
            try {
                if(estado){
                    synchronized (sincronizador){
                        sincronizador.wait();
                        
                        esperar(false);    
                    }
                }
                Thread.sleep(1);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

        }

    }

    public void fight(Immortal i2) { 
        if (i2.getHealth() > 0) {            
            i2.changeHealth(i2.getHealth() - defaultDamageValue);
            this.changeHealth(this.getHealth() + defaultDamageValue);
            updateCallback.processReport("Fight: " + this + " vs " + i2+"\n");

        } else {
            updateCallback.processReport(this + " says:" + i2 + " is already dead!\n");
            i2.matar();
            immortalsPopulation.remove(i2);
        }

    }
    public void sincronicePelea(Immortal i2){        
        int thisHash = System.identityHashCode(this);
        int i2Hash = System.identityHashCode(i2);
        if (thisHash < i2Hash) {
            synchronized (this) {
                synchronized (i2) {
                    this.fight(i2);
                }
            }
        } else if (thisHash > i2Hash) {
            synchronized (i2) {
                synchronized (this) {
                    this.fight(i2);
                }
            }
        } else {
            synchronized (desempatar) {
                synchronized (this) {
                    synchronized (i2) {
                        this.fight(i2);
                    }
                }
            }
        }
    
    }

    public void changeHealth(int v) {
        health = v;
    }

    public int getHealth() {
        return health;
    }

    @Override
    public String toString() {

        return name + "[" + health + "]";
    }
    public void esperar(boolean estado){
        this.estado = estado;
    }
    public boolean estaVivo(){
        return this.vivo;
    }
    private void matar(){
        this.vivo = false;
        
    }
}
