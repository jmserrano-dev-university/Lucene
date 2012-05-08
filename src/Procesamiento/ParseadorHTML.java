/**
 * Clase ParseadorHTML.java
 * @author José Manuel Serrano Mármol
 * @author Raul Salazar de Torres
 */
package Procesamiento;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class ParseadorHTML {
    
    public static List<Documento> ejecutarParserHTML(String rutaDirectorio) {
        List<Documento> listaDocumentos = new ArrayList<Documento>();
        File directorio = new File(rutaDirectorio);
        String [] ficheros = directorio.list();
        
        //Leemos los fichero que contiene el directorio
        Runtime sistema = Runtime.getRuntime();
        
        for(int i = 0; i < ficheros.length; i++){
            try {
                //Parseamos lo html
                Process proceso = sistema.exec("htmlparser1_6/bin/stringextractor " + rutaDirectorio+"/"+ficheros[i]);
                
                //Parseamos el cuerpo del HTML
                BufferedReader buffer = new BufferedReader(new InputStreamReader(proceso.getInputStream()));
                String linea, cuerpo = "";
                while((linea = buffer.readLine()) != null){
                    cuerpo = cuerpo + linea + "\n";
                }
                
                //Parseamos el titulo del HTML
                String titulo;
                proceso = sistema.exec("java -jar htmlparser1_6/lib/htmlparser.jar " + rutaDirectorio+"/"+ficheros[i] + " title");
                buffer = new BufferedReader(new InputStreamReader(proceso.getInputStream()));
                titulo = buffer.readLine();
                
                //Introducimos los documentos en la lista
                Documento doc = new Documento(titulo, cuerpo, ficheros[i]);
                listaDocumentos.add(doc);
                
            } catch (IOException ex) {
                System.out.println("Error al parsera el archivo " + ficheros[i]);
            }
        }
        return listaDocumentos;
    }
}  