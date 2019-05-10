package ensamblador.io;


import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import javax.swing.JTextArea;

import java.io.FileWriter;
import java.awt.Desktop;
import javax.swing.JTextArea;
import java.io.*;

public class Archivos extends Exception
{
    public static void guardar(String contenido, String ruta) throws IOException
    {
        FileWriter fw = new FileWriter(ruta);
        fw.write(contenido);
        fw.close(); 
    }
    public static String errorCompilacion() throws Exception
    {
        String mensajeError =  new String(Files.readAllBytes(Paths.get("./ensamblador_temp/error.txt")));
        return  mensajeError;
    }
    public static String cargar(String ruta) throws FileNotFoundException,IOException
    {
       String texto = "";
       StringBuilder contenido = new StringBuilder();
        FileReader fr = new FileReader(ruta);
        BufferedReader br = new BufferedReader(fr);
        while((texto = br.readLine())!=null)
        {
            contenido.append(texto)
                    .append("\n");
        }
        br.close();

        return contenido.toString();
    }
}