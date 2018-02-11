package Genetico;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;
import Codificacion.Individuo;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Collections;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;


/**
 *
 * @author Bernardo Pulido
 */
public class Minimizar {
    
    private int epocas;
    private double probMuta;
    private double probCruza;
    private int tamPoblacion;
    private int tamMuestra;
    private int dimension;
    private int valor_max;
    private LinkedList<Individuo> poblacion;
    public ArrayList<Integer> semilla;
    private LinkedList<Individuo> muestraPoblacion;
    private ArrayList<Integer> aptitudes;
    private ArrayList<ArrayList<Integer>> flujos;
    private ArrayList<ArrayList<Integer>> distancias;
   
    public Minimizar(int epocas, double probMuta, double probCruza, int tamPoblacion, int n) {
        this.epocas = epocas;
        this.probMuta = probMuta;
        this.probCruza = probCruza;
        switch(n){
            case 0: this.dimension=20;
                    this.valor_max=3683;
            break;
            case 1: this.dimension=30;
                    this.valor_max=88900;
            break;
            case 2: this.dimension=35;
                    this.valor_max=283315445;
            break;
            case 3: this.dimension=40;
                    this.valor_max=240516;
            break;
        }
       
        this.tamPoblacion = tamPoblacion;
        this.tamMuestra = 10;
        poblacion = new LinkedList<Individuo>();
        this.semilla = new ArrayList<Integer>();
        this.muestraPoblacion = new LinkedList<Individuo>();
        this.aptitudes = new ArrayList<Integer>();
        this.flujos = new ArrayList<ArrayList<Integer>>();
        this.distancias = new ArrayList<ArrayList<Integer>>();
        generarMatrices(n);
        generarPoblacionIncial(true);
        start();
    }
    
    public void generarSemilla(){
        for (int x=1;x <=this.dimension;x++){
            this.semilla.add(x);
        }
    }
    public void imprimirMatrices(){
        for(int i=0; i<this.flujos.size();i++){
            for(int j=0; j<this.flujos.size();j++){
                System.out.print(" "+this.flujos.get(i).get(j)+" ");
            }
            System.out.println("");
        }
         System.out.println("");
        for(int i=0; i<this.distancias.size();i++){
            for(int j=0; j<this.distancias.size();j++){
                System.out.print(" "+this.distancias.get(i).get(j)+" ");
            }
             System.out.println("");
        }
    }
    
    private void generarMatrices(int n){
        String archivo = "";
        
        switch(n){
            case 0: archivo = "lipa20a.txt";
                    break;
                    
            case 1: archivo = "kra30a.txt";
                    break;
                    
            case 2: archivo = "Tai35b.txt";
                    break;
                    
            case 3: archivo = "Tho40.txt";
                    break;
                   
        }
        FileReader f = null;
        try {
            String cadena;
            f = new FileReader(archivo);
            BufferedReader b = new BufferedReader(f);
            int i=0;
            while((cadena = b.readLine())!=null || i<(this.dimension*2)) {
                if(i<this.dimension){
                    ArrayList<Integer> temp = new ArrayList<Integer>();
                    String[] cadena_array = cadena.split("\\s+");
                    for (String a:cadena_array){
                        
                           temp.add(Integer.parseInt(a));    
                     }
                    this.flujos.add(temp);
                    i++;
                }else{
                    ArrayList<Integer> temp = new ArrayList<Integer>();
                    
                    String[] cadena_array = cadena.split("\\s+");
                    for (String a:cadena_array){
                           temp.add(Integer.parseInt(a));    
                     }
                    this.distancias.add(temp);
                    i++;
                }

            }
            b.close();
        } catch (FileNotFoundException ex) {
            Logger.getLogger(Minimizar.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(Minimizar.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                f.close();
            } catch (IOException ex) {
                Logger.getLogger(Minimizar.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
    
    private void generarPoblacionIncial(boolean ok) {
        if(ok){
            generarSemilla();
        }
        
        for (int x = 0; x < this.tamPoblacion; x++) {
            this.poblacion.add(new Individuo(this.semilla, this.flujos, this.distancias)); 
        }
    }
        
    private void Test() {
        ArrayList<Integer> ahs = new ArrayList<Integer>();
        ahs.add(19);ahs.add(17);ahs.add(7);ahs.add(1);ahs.add(5);
        ahs.add(9);ahs.add(10);ahs.add(12);ahs.add(4);ahs.add(16);
        ahs.add(20);ahs.add(6);ahs.add(3);ahs.add(14);ahs.add(11);
        ahs.add(15);ahs.add(13);ahs.add(8);ahs.add(2);ahs.add(18);

        Individuo in = new Individuo(ahs, 20, flujos, distancias);
        System.out.println("Fitness: "+in.getFitness());
        System.out.println("Genotipo: "+in.getGenotipo());

    }
    
    public void start() {
        // Se recorre el número de épocas
        int recurrencias=0;
        for (int x = 0; x < this.epocas; x++) {
           Individuo best = getMejorGeneracion();
           if(x>0){
                if(best.getFitness()==this.aptitudes.get(this.aptitudes.size()-1)){
                    recurrencias++;

                }else{
                    recurrencias=0;
                }  
           }

           this.aptitudes.add(best.getFitness());
           
           //System.out.println("Epoca:"+x+" Fitness: "+best.getFitness());
           if(best.getFitness()==this.valor_max){
                   System.out.println("Genotipo resultante: "+best.getGenotipo());
                   break;
           }

           Random ran = new Random();   

            if(recurrencias>1000){
                recurrencias=0;
                //this.probMuta=this.probMuta+0.025;
                
                for(int m=0; m<this.tamPoblacion/2;m++){
                    int mm = ran.nextInt(this.tamPoblacion);
                    this.poblacion.set(mm, new Individuo(semilla, flujos, distancias));
                } 
            }


            this.poblacion.set(ran.nextInt(this.tamPoblacion), best);
            

            for(int k=0; k<3;k++){
                    //selección de cinco individuos aleatorios
                    for (int y = 0; y < this.tamMuestra; y++) {
                       muestraPoblacion.add(this.poblacion.get(ran.nextInt(this.tamPoblacion)));
                    }  

                    //selección del padre
                    Individuo padre = seleccion();
                    //selección de la madre
                    Individuo madre = seleccion();
                    //Cruza        
                    int point = ran.nextInt(this.dimension);

                    Individuo hijo1 = cruza(padre, madre, point);
                    Individuo hijo2 = cruza(madre, padre, point);
                    //Muta
                    hijo1 = muta(hijo1);
                  
                    hijo2 = muta(hijo2);
                    //Reemplazo
                    reemplazo(hijo1);
                    reemplazo(hijo2);

                    muestraPoblacion.clear();
           }
           
           
            if(x==this.epocas-1)
            System.out.println("Genotipo resultante: "+best.getGenotipo());
        }
        
    }

    private Individuo reemplazo_elitista(Individuo padre, Individuo madre, Individuo hijo){
        ArrayList<Individuo> temp = new ArrayList<>();
        temp.add(padre);
        temp.add(madre);
        temp.add(hijo);
       
        int min=0;
        for(int i=1; i<3; i++){
            if(temp.get(i).getFitness()<temp.get(min).getFitness()){
                min=i;
            }
        }
      
        return temp.get(min);
    }
    
    private Individuo seleccion_g(){
        Random r =new Random();
        int pos = r.nextInt(this.tamPoblacion);
        
        return this.poblacion.get(pos);
    }
    
    //Reemplazo del peor individuo 
    private void reemplazo(Individuo hijo){
        int max=0;
        for(int i = 1; i < this.tamPoblacion; i++){
            if(this.poblacion.get(i).getFitness() > this.poblacion.get(max).getFitness()){
                max = i;
                
            }
        }
        
        //if(hijo.getFitness() < this.poblacion.get(max).getFitness()){
            this.poblacion.set(max, hijo);
        //} 
    }
    
    private Individuo seleccion() {
           
        int min=0;
        Individuo select;
        for(int z = 1;z<this.muestraPoblacion.size(); z++){
            if(muestraPoblacion.get(z).getFitness() < muestraPoblacion.get(min).getFitness()){
                min=z;
            }
        }
        select = muestraPoblacion.get(min);
        muestraPoblacion.remove(min);
        

        return select;
    }

    
    /**
     * 
     * Cruza
     */
    private Individuo cruza(Individuo padre, Individuo madre, int point) {
                                
            ArrayList<Integer> genotipo =new ArrayList<>();

            List<Integer> sub1 = padre.getGenotipo().subList(0, point);

            for (Integer a:sub1){
               genotipo.add(a);    
            }

            while(genotipo.size() < this.dimension){
                if(!genotipo.contains(madre.getGenotipo().get(point))){
                    genotipo.add(madre.getGenotipo().get(point));
                }else{
                    point++;
                    if(point==this.dimension){
                        point=0;
                    }
                }
            }
            Individuo hijo = new Individuo(genotipo, dimension, this.flujos, this.distancias);
            
            return hijo;
    }

    
    private Individuo cruza_cycle(Individuo padre, Individuo madre, int point){
        
        Random rnd = new Random();
        float f = rnd.nextFloat();
                
        if (f<=this.probCruza){
            
            Random ran = new Random();

            final List<Integer> cycleIndices = new Vector<Integer>();

            int t = ran.nextInt(padre.getGenotipo().size()-1);
            cycleIndices.add(t);

            Integer cycle = madre.getGenotipo().get(t);

            t = padre.getGenotipo().indexOf(cycle);

            while (t != cycleIndices.get(0)) {
              cycleIndices.add(t);
              cycle = madre.getGenotipo().get(t);
              t = padre.getGenotipo().indexOf(cycle);
            }

            ArrayList<Integer> genotipo =new ArrayList<>();
            genotipo = padre.getGenotipo();
            for (final int index : cycleIndices) {
                genotipo.set(index, madre.getGenotipo().get(index));
            }
            Individuo hijo = new Individuo(genotipo, dimension, this.flujos, this.distancias);

            return hijo;  
        }else{
            if(padre.getFitness()<madre.getFitness()){
                return padre;
            }else{
                return madre;
            }
        }
    }

    private Individuo muta(Individuo hijo) {
        
        Random rand = new Random();
        float f = rand.nextFloat();
        
        if (f<=this.probMuta){
        	
            int pos=rand.nextInt(dimension);
            int pos2 = rand.nextInt(dimension);
        			
            Integer valor = (Integer)hijo.getGenotipo().get(pos);
            Integer valor2 = (Integer)hijo.getGenotipo().get(pos2);
            hijo.getGenotipo().set((int)pos, valor2);
            hijo.getGenotipo().set((int)pos2, valor);  
            hijo.calculaFitness();
            hijo.toString();
        }
        return hijo;
    }
    
    private Individuo muta_insert(Individuo hijo){
        Random ran = new Random();
        float f = ran.nextFloat();
        
        if (f<=this.probMuta){
            int pos=ran.nextInt(dimension);
            int pos2 = ran.nextInt(dimension);
                        
            //Asegurar pos<=pos2
            if(pos>pos2){
                int buffer = pos2;
                pos2=pos;
                pos=buffer;
            }
            
            if(pos!=pos2){	
                Integer valor = (Integer)hijo.getGenotipo().get(pos);
                Integer valor2 = (Integer)hijo.getGenotipo().get(pos2);
                hijo.getGenotipo().set((int)pos, valor2);
                hijo.getGenotipo().remove(pos2);
                hijo.getGenotipo().add(pos+1, valor);

                hijo.calculaFitness();
                hijo.toString();
            }

        }
        return hijo;
    }
    
    private Individuo muta_scrambled(Individuo hijo){

        Random ran = new Random();
        float f = ran.nextFloat();
        
        if(f<=this.probMuta){
            int pos = ran.nextInt(dimension);
            int pos2 = ran.nextInt(dimension);            
            
            List<Integer> subset=new ArrayList<Integer>();;
            if(pos<=pos2){
                   subset = hijo.getGenotipo().subList(pos, pos2+1);
            }else{
                 List<Integer> buffer1 = hijo.getGenotipo().subList(pos, dimension);
                 List<Integer> buffer2 = hijo.getGenotipo().subList(0, pos2+1);
                 
                 subset.addAll(buffer1);
                 subset.addAll(buffer2);
            }
     
            Collections.shuffle(subset);
            
            if(pos>pos2){
                int cont=0;
                for(int i=pos; i<dimension;i++){
                    hijo.getGenotipo().set(i, subset.get(cont));                 
                    cont++;
                }
                for(int i=0; i<pos2+1;i++){
                    hijo.getGenotipo().set(i, subset.get(cont));
                    cont++;
                }
            }else{               
                int cont=0;
                for(int i=pos; i<(pos2-pos+1);i++){
                    hijo.getGenotipo().set(i, subset.get(cont));
                    cont++;
                }
            }
            
            hijo.calculaFitness();
            hijo.toString();
        }
        return hijo;
    }
    
    public Individuo muta_inversa_circular(Individuo hijo){
     Random ran = new Random();
        float f = ran.nextFloat();
        
        if(f<=this.probMuta){
            int pos = ran.nextInt(dimension);
            int pos2 = ran.nextInt(dimension);      
            
            List<Integer> subset=new ArrayList<Integer>();;
            if(pos<=pos2){
                   subset = hijo.getGenotipo().subList(pos, pos2+1);
            }else{
                 List<Integer> buffer1 = hijo.getGenotipo().subList(pos, dimension);
                 List<Integer> buffer2 = hijo.getGenotipo().subList(0, pos2+1);
                 
                 subset.addAll(buffer1);
                 subset.addAll(buffer2);
            }
     
            Collections.reverse(subset);
            
            if(pos>pos2){
                int cont=0;
                for(int i=pos; i<dimension;i++){
                    hijo.getGenotipo().set(i, subset.get(cont));                 
                    cont++;
                }
                for(int i=0; i<pos2+1;i++){
                    hijo.getGenotipo().set(i, subset.get(cont));
                    cont++;
                }
            }else{               
                int cont=0;
                for(int i=pos; i<(pos2-pos+1);i++){
                    hijo.getGenotipo().set(i, subset.get(cont));
                    cont++;
                }
            }

            hijo.calculaFitness();
            hijo.toString();
        }
        return hijo;
    }
    
    public Individuo muta_inversas(Individuo hijo){
        Random ran = new Random();
        float f = ran.nextFloat();
        
        if(f<=this.probMuta){
            int pos = ran.nextInt(dimension);
            int pos2 = ran.nextInt(dimension);
            
            //Asegurar pos<=pos2
            if(pos>pos2){
                int buffer = pos2;
                pos2=pos;
                pos=buffer;
            }
            
            int diff = pos2-pos+1;
            
            List<Integer> subset = hijo.getGenotipo().subList(pos, pos2+1); 
            Collections.reverse(subset);
            
            int cont=0;
            for(int i=pos; i<diff;i++){
                hijo.getGenotipo().set(i, subset.get(cont));
                cont++;
            }
                        
            hijo.calculaFitness();
            hijo.toString();
        }

        return hijo;
    }
    
    public Individuo getMejorGeneracion(){
        int min=0;
        for(int i = 1; i < this.tamPoblacion;i++){
            if(this.poblacion.get(i).getFitness() < this.poblacion.get(min).getFitness()){
                min = i;
            }
        }
        
        return this.poblacion.get(min);
    }
    public int getPeorGeneracion(){
        int max=0;
        for(int i = 1; i < this.tamPoblacion;i++){
            if(this.poblacion.get(i).getFitness() > this.poblacion.get(max).getFitness()){
                max = i;
            }
        }
        
        return max;
    }
    
    public ArrayList<Integer> getAptitudes(){
        return this.aptitudes;
    }
}