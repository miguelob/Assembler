package ensamblador.languages;

import java.util.HashMap;
import java.util.Scanner;
import java.util.Collection;
import java.util.ArrayList;
import java.util.ArrayList;
import java.io.*;
import java.io.FileInputStream;
import java.io.File;
import java.io.IOException;
import java.util.Scanner;


public class Languages
{
    public final static String SPANISH = "esp_eng/Spanish.txt";
    public final static String ENGLISH = "esp_eng/English.txt";
    private HashMap<String,String> hmLanguages = new HashMap<String,String>();
    private Scanner lectura;

    public Languages(String ruta)
    {
        try{
            lectura = new Scanner(new File(ruta));
            lectura.useDelimiter("::");
            String clave=null;
            String asociado=null;

            while(lectura.hasNext())
            {
                clave = lectura.next();
                asociado = lectura.nextLine();
                asociado = asociado.replace("::","");
                hmLanguages.put(clave,asociado);
            }
            lectura.close();    
        }catch(Exception e)
        {
            //A RELLENAR
            e.printStackTrace();
        }   
    }
    public HashMap<String,String> getLanguage()
    {
        return hmLanguages;
    }
}
