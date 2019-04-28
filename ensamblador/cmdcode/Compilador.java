package ensamblador.cmdcode;

import java.lang.Runtime;
import java.io.*;


public class Compilador extends Exception 
{
    private static String os = null; //No cambias de sistema operativo sin cerrar java
    public static void compilar(String file) throws IOException,InterruptedException
    {
        if(os == null) //con null se puede hacer ==
            os = System.getProperty("os.name");
        if(os.startsWith("Windows")){
            Process carpeta = Runtime.getRuntime().exec(new String[] {"cmd","/c","mkdir .\\ensamblador_temp"});
            carpeta.waitFor();
            Process codigo = Runtime.getRuntime().exec(new String[] {"cmd","/c", "assembler_source\\Ensamblador.exe "+"\""+file+"\""+" 2> .\\ensamblador_temp\\error.txt"});
            codigo.waitFor();
            Process mover = Runtime.getRuntime().exec(new String[] {"cmd","/c","move ROM.vhd .\\ensamblador_temp"});
            mover.waitFor();
        } else if(os.startsWith("Unix") || os.startsWith("MacOS") || os.startsWith("Linux")){
            Process carpeta = Runtime.getRuntime().exec(new String[] {"bash","-c","mkdir ./ensamblador_temp"});
            carpeta.waitFor();
            Process codigo = Runtime.getRuntime().exec(new String[] {"bash","-c", "assembler_source/Ensamblador "+file+" 2> ./ensamblador_temp/error.txt"});
            codigo.waitFor();
            Process mover = Runtime.getRuntime().exec(new String[] {"bash","-c","mv ROM.vhd ./ensamblador_temp"});
            mover.waitFor();
        }
    }
    public static void cambiarDirectorio(String path) throws IOException,InterruptedException
    {
        if(os.startsWith("Windows")){
            Process p = Runtime.getRuntime().exec(new String[] {"cmd","/c","move .\\ensamblador_temp\\ROM.vhd "+"\""+path+"\""});
            p.waitFor(); 
        }else if(os.startsWith("Unix") || os.startsWith("MacOS") || os.startsWith("Linux")){
            Process p =Runtime.getRuntime().exec(new String[] {"bash","-c","mv ./ensamblador_temp"+path});
            p.waitFor();
        }
    }
    public static void borrarDirectorio() throws IOException,InterruptedException
    {
        if(os.startsWith("Windows")){
            Process p = Runtime.getRuntime().exec(new String[] {"cmd","/c","rmdir ensamblador_temp /s /q"});
            p.waitFor(); 
        }else if(os.startsWith("Unix") || os.startsWith("MacOS") || os.startsWith("Linux")){
            Process p =Runtime.getRuntime().exec(new String[] {"bash","-c","rm -r ensamblador_temp"});
            p.waitFor();
        } 
    }
    
}