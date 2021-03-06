# Laboratorio 2 ARSW
## Integrantes:

- Juan Garcia
- Johan Arias
- Andrés Marcelo
- Simón Marin

# Como ejecutar el programa?(Parte I Start Production)
Primero que todo asegurese de tener Maven instalado en su equipo y tenerlo en el path de su S.O

Abra la consola y situese en donde descargo el programa, deberia quedarle algo asi el path: ``C:\Users\UExample\Downloads\Lab2ARSW\pi>``

- Para compilar el proyecto ponga el siguiente comando: ``mvn compile``
- Para ejecutar las pruebas ponga el siguiente comando: `` mvn test``
- Para probar el programa usted mismo y poner casos ponga el siguiente comando:
``mvn exec:java -Dexec.mainClass=" edu.eci.arst.concprg.prodcons.StartProduction``


## Part I - Before Finishing class
Check the operation of the program and run it. While this occurs, run jVisualVM and check the CPU consumption of the corresponding process. Why is this consumption? Which is the responsible class? 
> El problema nace del loop en el cual Consumer se mantiene preguntando si hay elementos en la queue.

![código](https://github.com/IJuanchoG/ARSW_LAB2/blob/master/images/SinModificar.png)
![Uso de Core](https://github.com/IJuanchoG/ARSW_LAB2/blob/master/images/SinModificar2.png)

Modificamos el código agregando sus respectivos Wait/Notify y sincronizamos lo necesario.

![código M](https://github.com/IJuanchoG/ARSW_LAB2/blob/master/images/Modificado.png)
![Uso de Core M](https://github.com/IJuanchoG/ARSW_LAB2/blob/master/images/Modificado2.png)

## Part II - Synchronization and Dead-Locks.

Review the code and identify how the functionality indicated above was implemented. Given the intention of the game, an invariant should be that the sum of the life points of all players is always the same (of course, in an instant of time in which a time increase / reduction operation is not in process ). For this case, for N players, what should this value be?

> el invariante de la vida que deberían tener los jugadores es N*100

Run the application and verify how the ‘pause and check’ option works. Is the invariant fulfilled?

> Debido a la falta de sincronización en los valores no hay un control en el invariante lo que provoca que no se cumpla.

Check the operation again (click the button many times). Is the invariant fulfilled or not ?.

> Ya no falla de tal manera como el caso anterior

Analyzing the simulation operation scheme, could this create a race condition? 

>Si, esto debido a que un inmortal puede tener una lucha con un jugador muerto antes de
>que cambie a dicho estado, esto permitiría que el jugador ya muerto pudiese obtener puntos
>de vida extra, lo que lo volvería a "reanimar".

