package Codificacion;

/**
 *
 * @author Bernardo Pulido
 */
import java.util.ArrayList;
import java.util.Collections;


public class Individuo {
	
    private ArrayList<Integer> genotipo;
    private int fenotipo;
    private int fitness;
    private int dimension;
    private String cadenaGenotipo;
    private int[] colisiones;
    private ArrayList<ArrayList<Integer>> flujos;
    private ArrayList<ArrayList<Integer>> distancias;

    public Individuo(ArrayList<Integer> genotipo, int n, ArrayList<ArrayList<Integer>> flujos, ArrayList<ArrayList<Integer>> distancias){
        this.genotipo = genotipo;
        this.dimension = n;
        this.colisiones = new int[2];
        this.flujos = flujos;
        this.distancias = distancias;
        calculaFitness();
        toString();
    }
       
    // Constructor por defecto
    public Individuo(ArrayList<Integer> semilla, ArrayList<ArrayList<Integer>> flujos, ArrayList<ArrayList<Integer>> distancias) {
      genotipo = new ArrayList<Integer>();
      this.dimension = semilla.size();
      this.colisiones = new int[2];
      this.cadenaGenotipo = "";
      this.flujos = flujos;
      this.distancias = distancias;
      GenerarGenotipoAleatorio(semilla);
      calculaFitness();
      toString();
    }
       
    private void GenerarGenotipoAleatorio(ArrayList<Integer> semilla) {
               
        Collections.shuffle(semilla);
        for (int x=0;x <this.dimension;x++){
            this.genotipo.add(semilla.get(x));
        }  
    }
       
    public void calculaFitness(){
        int fi=0;
		
        for (int i = 0; i < this.dimension; i++){
                for (int j = 0; j < this.dimension; j++){
                        if (i != j) {
                                int f1 = this.genotipo.indexOf(i+1);
                                int f2 = this.genotipo.indexOf(j+1);
                                fi += (this.distancias.get(i).get(j) * this.flujos.get(f1).get(f2));
                        }
                } 
        } 
        this.fitness=fi;
    }

    @Override
    public String toString() {
            this.cadenaGenotipo = "";   
              for (Integer a: this.genotipo){
                this.cadenaGenotipo = this.cadenaGenotipo + a;
              }
             return this.cadenaGenotipo;
    }
    
    public ArrayList<Integer> getGenotipo() {
            return genotipo;
    }

    public void setGenotipo(ArrayList<Integer> genotipo) {
            this.genotipo = genotipo;
    }

    public int getFenotipo() {
            return fenotipo;
    }

    public int getFitness() {
        return this.fitness;
    }

    public int getDimension() {
            return dimension;
    }

    public int[] getColisiones() {
            return colisiones;
    }
       
}