/**
 * Clase AppFachada.java
 * @author José Manuel Serrano Mármol
 * @author Raul Salazar de Torres
 */
package Interfaz;

import Procesamiento.*;
import java.io.*;
import java.util.ArrayList;
import java.util.List;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.Token;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.standard.StandardAnalyzer;

public class AppFachada {
    
    public void ejecutar() throws IOException{
        List<Documento> listaDocumentos = new ArrayList<Documento>();
        listaDocumentos = ParseadorHTML.ejecutarParserHTML(Rutas.RUTA_ARCHIVOS_ORIGINALES);

        //Menú
        System.out.println("¿Que desea realizar?");
        System.out.println("\t 1.- Procesar archivos");
        System.out.println("\t 2.- Consultar información");
        int opcionFuncion = captarOpcion(2);
        String consulta = "";
        if(opcionFuncion == 2){
            System.out.println("¿Que Consulta dese realizar?");
            consulta = captarConsulta();
        }
        
        System.out.println("¿En que idioma desea realizar las consultas?");
        System.out.println("\t 1.- Español");
        System.out.println("\t 2.- Inglés");
        int opcionIdioma = captarOpcion(2);
        
        //******************** Realizamos el procesamiento
        Analyzer analizador;
        String ruta = "";
        String rutaIndexador = "";
        if(opcionIdioma == 1){
            analizador = new SpanishAnalyzer();
            ParseadorHTML.escribirDocumentosDisco(listaDocumentos,true);
            ruta = Rutas.RUTA_ARCHIVOS_PROCESADOS_SPANISH;
            rutaIndexador = Rutas.RUTA_INDEXADOR_SPANISH;
        }else{
            analizador = new StandardAnalyzer();
            ParseadorHTML.escribirDocumentosDisco(listaDocumentos,false);
            ruta = Rutas.RUTA_ARCHIVOS_PROCESADOS_ENGLISH;
            rutaIndexador = Rutas.RUTA_INDEXADOR_ENGLISH;
        }
        
        if(opcionFuncion == 1){
            for(int i = 0; i < listaDocumentos.size(); i++){
                try {
                    TokenStream resultado = analizador.tokenStream("content", new FileReader(ruta + listaDocumentos.get(i).getNombreDocumento()));

                    //Recorremos los token y los almacenamos
                    Token t;
                    String tokens = "";
                    while((t=resultado.next()) != null){
                        tokens = tokens + " "+ t.term();
                    }
                    listaDocumentos.get(i).setCuerpo(tokens);

                } catch (FileNotFoundException ex) {
                    System.out.println("Error al leer archivo en procesamiento");
                }
            }

            //Escribimos de nuevo los archivos - PREPROCESADOS
            if(ruta.equalsIgnoreCase(Rutas.RUTA_ARCHIVOS_PROCESADOS_SPANISH)){
                ParseadorHTML.escribirDocumentosDisco(listaDocumentos,true);
            }else{
                ParseadorHTML.escribirDocumentosDisco(listaDocumentos,false);
            }
            

            //Indexamos los archivos
            Indexer indexador = new Indexer(rutaIndexador, analizador);
            try {
                indexador.index(ruta);
                indexador.close();
            } catch (Exception ex) {
                System.out.println("ERROR indexando archivos");
            }
            
        }else{
            //******************** Realizamos la búsqueda
            try {
                Searcher.search(rutaIndexador, consulta,analizador);
            } catch (Exception ex) {
                System.out.println("ERROR al realizar la búsqueda");
            }
        }
    }
    
    
    private int captarOpcion(int limite){
        try {
            BufferedReader teclado = new BufferedReader(new InputStreamReader(System.in));
            System.out.print("\nIndique una opción: ");
            int opcion = Integer.parseInt(teclado.readLine());
            
            if(opcion >=0 && opcion <= limite){
                return opcion;
            }else{
                throw new Exception();
            }
            
        } catch (Exception ex) {    
            System.out.println("Valor NO permitido");
            return captarOpcion(limite);
        }
    }
    
    private String captarConsulta(){
        String consulta = "";
        BufferedReader teclado = new BufferedReader(new InputStreamReader(System.in));
        System.out.print("\nIndique consulta: ");
        try {
            consulta = teclado.readLine();
            
        } catch (IOException ex) {
            System.out.println("ERROR al leer consulta desde teclado");
        }
        
        return consulta;
    }
    
}
