import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

/**
 * Created by Sidnei on 02/04/2017.
 */
public class Histograma
{
    public static final String PATH = "C:\\Users\\Sidnei\\Documents\\Prog3d\\img\\img\\gray";

    int [] calcularHistograma(BufferedImage img)
    {
        int[] histograma = new int[256];

        for(int y = 0; y <img.getHeight(); y++)
        {
            for(int x = 0; x < img.getWidth(); x++)
            {
                Color color = new Color(img.getRGB(x,y));
                int red = color.getRed();
                histograma[red] += 1;
            }
        }
        return histograma;
    }
    public int [] calcularHistogramaAcumulado(int [] histograma)
    {
        int [] acumulado = new int[256];
        acumulado[0] = histograma[0];

        for(int i = 1; i < histograma.length; i++)
        {
            acumulado[i] = histograma[i] + acumulado[i- 1];

        }
        return acumulado;
    }
    private int menorValor(int [] histograma)
    {
        for(int i = 0; i < histograma.length; i++)
        {
            if(histograma[i] != 0)
                return i;
        }
        return 0;
    }


    private int [] calcularMapadeCores(int [] histograma, int pixels)
    {
        int [] mapaDeCores = new int[256];
        int [] acumulado = calcularHistogramaAcumulado(histograma);
        float menor = menorValor(histograma);

        for(int i = 0; i <histograma.length; i++)
        {
            mapaDeCores[i] = Math.round(((acumulado[i] - menor) / (pixels - menor)) * 255);
        }
        return mapaDeCores;
    }
    public BufferedImage equalize(BufferedImage img)
    {
        int [] histograma = calcularHistograma(img);
        int [] mapaDeCores = calcularMapadeCores(histograma, img.getWidth() * img.getHeight());

        BufferedImage out = new BufferedImage(img.getWidth(), img.getHeight(), BufferedImage.TYPE_INT_RGB);

        for(int y = 0; y < img.getHeight(); y++)
        {
            for( int x = 0; x < img.getWidth(); x++)
            {
                Color color = new Color(img.getRGB(x,y));
                int tom = color.getRed();

                int newTom = mapaDeCores[tom];
                Color newColor = new Color(newTom, newTom, newTom);

                out.setRGB(x,y,newColor.getRGB());
            }
        }
        return out;
    }
    void run() throws IOException {
        BufferedImage img = ImageIO.read(new File(PATH, "montanha.jpg"));
        BufferedImage newImg = equalize(img);

        ImageIO.write(newImg, "jpg", new File(PATH, "montanhaE.jpg"));
    }

    public static void main(String[] args) throws IOException
    {
        new Histograma().run();
    }
}
