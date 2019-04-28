package ensamblador.images;

import java.awt.Image;
import javax.swing.ImageIcon;

public class Images
{
    public static ImageIcon resize(ImageIcon icono,int alto,int ancho){
        Image image = icono.getImage(); // transform it 
        Image newimg = image.getScaledInstance(25, 25,  java.awt.Image.SCALE_SMOOTH); // scale it the smooth way  
        icono = new ImageIcon(newimg);
        return icono;
    }
}