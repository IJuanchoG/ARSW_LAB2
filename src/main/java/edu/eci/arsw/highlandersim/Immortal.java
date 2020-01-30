package edu.eci.arsw.highlandersim;

import java.util.List;
import java.util.Random;
import java.util.concurrent.Semaphore;

public class Immortal extends Thread {

    private ImmortalUpdateReportCallback updateCallback=null;
    
    private int health;
    
    private int defaultDamageValue;

    private final List<Immortal> immortalsPopulation;

    private final String name;

    private final Random r = new Random(System.currentTimeMillis());
    
     private Semaphore mutex;

    public Immortal(String name, List<Immortal> immortalsPopulation, int health, int defaultDamageValue, ImmortalUpdateReportCallback ucb) {
        super(name);
        this.updateCallback=ucb;
        this.name = name;
        this.immortalsPopulation = immortalsPopulation;
        this.health = health;
        this.defaultDamageValue=defaultDamageValue;
        this.mutex = new Semaphore(immortalsPopulation.size()-1);
    }

    public void run() {

        while (true) {
            Immortal im;

            int myIndex = immortalsPopulation.indexOf(this);

            int nextFighterIndex = r.nextInt(immortalsPopulation.size());

            //avoid self-fight
            if (nextFighterIndex == myIndex) {
                nextFighterIndex = ((nextFighterIndex + 1) % immortalsPopulation.size());
            }

            im = immortalsPopulation.get(nextFighterIndex);
            if(im.isAlive()){
                this.fight(im);
            }
            
            try {
                Thread.sleep(1);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

        }

    }

    public void fight(Immortal i2) { 
        try {
            mutex.acquire();
            if (i2.getHealth() > 0) {            
                i2.changeHealth(i2.getHealth() - defaultDamageValue);
                this.changeHealth(this.getHealth() + defaultDamageValue);
                updateCallback.processReport("Fight: " + this + " vs " + i2+"\n");

            } else {
                updateCallback.processReport(this + " says:" + i2 + " is already dead!\n");
            }

        } catch (InterruptedException e) {
            // exception handling code
        } finally {
            mutex.release();
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

}
