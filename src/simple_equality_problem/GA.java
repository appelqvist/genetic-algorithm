package simple_equality_problem;

import java.util.ArrayList;
import java.util.LinkedList;

/**
 * Created by A Appelqvist
 */

public class GA {
    private float crossoverRate, mutationRate;
    private ArrayList<int[]> population;
    private int[] bestChromosome;
    private float bestChromosomeFitness;

    public GA(int population, float crossoverRate, float mutationRate){
        this.population = new ArrayList<>();
        this.crossoverRate = crossoverRate;
        this.mutationRate = mutationRate;

        //Initialize the chromosomes
        for(int i = 0; i < population; i++){
            int[] chromosome = {
                    Util.randInt(0,30),
                    Util.randInt(0,30),
                    Util.randInt(0,30),
                    Util.randInt(0,30)
            };
            this.population.add(chromosome);
        }
    }

    public void calc(){
        int generation = 1;
        while (bestChromosomeFitness != 1f){
            selection();
            singlePointCrossover();
            mutation();
            generation++;
        }

        for(int n : bestChromosome){
            System.out.print(n+" ");
        }
        System.out.println("generations: "+generation);
    }

    private int[] evaluation(){
        int[] res = new int[this.population.size()];
        int[] temp;
        int chromosomesum;
        for(int i = 0; i < res.length; i++){
            temp = this.population.get(i);
            chromosomesum = (temp[0] + (2 * temp[1]) + (3 * temp[2]) + (4 * temp[3]) - 30);
            res[i] = chromosomesum;
        }
        return res;
    }

    public void selection(){
        int[] evaluationRes = evaluation();
        float[] fitness = new float[evaluationRes.length];
        float total = 0f;

        float temp;
        for(int i = 0; i < evaluationRes.length; i++){
            temp = ( 1f / (evaluationRes[i] + 1f) );
            total += temp;
            fitness[i] = temp;
            if(temp > bestChromosomeFitness){
                bestChromosome = new int[population.get(i).length];
                for(int j = 0; j < population.get(i).length; j++){
                    bestChromosome[j] = population.get(i)[j];
                }
                bestChromosomeFitness = temp;
            }
        }

        float[] probability = new float[fitness.length];
        for(int i = 0; i < fitness.length; i++){
            probability[i] = fitness[i]/total;
        }

        float[] cumulativeProbability = new float[probability.length];
        temp = 0f;
        for(int i = 0; i < probability.length; i++){
            cumulativeProbability[i] = (temp += probability[i]);
        }

        ArrayList<int[]> newPopulation = new ArrayList<>();
        float rand;
        int count;
        for(int i = 0; i < this.population.size(); i++){
            count = 0;
            rand = Util.randFloat();

            while(count < cumulativeProbability.length) {
                if (rand < cumulativeProbability[count] || count >= population.size()-1) {
                    break;
                }else {
                    count++;
                }
            }

            int[] chromosome = new int[population.get(0).length];
            for(int j = 0; j < chromosome.length; j++){
                chromosome[j] = population.get(count)[j];
            }
            newPopulation.add(chromosome);
        }

        this.population = null;
        this.population = newPopulation;
    }

    public void singlePointCrossover(){
        //See which one that going to be parents.
        LinkedList<Integer> parentsPosition = new LinkedList<>();
        for(int i = 0; i < population.size(); i++){
            float rnd = Util.randFloat();
            if(rnd < crossoverRate){
                parentsPosition.add(i);
            }
        }

        //If its just one parent skip it.
        if(parentsPosition.size() > 1){
            ArrayList<int[]> children = new ArrayList<>();
            int crossPoint;
            int chromosomeLength = population.get(0).length; //All chromosomes are the same length.
            int[] tempChromosome;
            for(int i = 0; i < parentsPosition.size(); i++){
                //Get the crossover position.
                crossPoint = Util.randInt(1, chromosomeLength-2);
                tempChromosome = new int[chromosomeLength];

                //Build new chromosome from parents.
                for(int j = 0; j < chromosomeLength; j++){
                    if(j < crossPoint){
                        tempChromosome[j] = population.get( parentsPosition.get(i) )[j];
                    }else{
                        tempChromosome[j] = population.get( parentsPosition.get( (i+1) % parentsPosition.size() ))[j];
                    }
                }
                children.add(tempChromosome);
            }

            for(int i = 0; i < parentsPosition.size(); i++){
                population.set(parentsPosition.get(i), children.get(i));
            }

        }
    }

    public void mutation(){
        int totalGen = population.size()*population.get(0).length;
        int nbrOfMutations = (int)Math.floor(totalGen * mutationRate);
        int pos;
        int cl = population.get(0).length; //All chromosomes are the same length.
        for(int i = 0; i < nbrOfMutations; i++){
            pos = Util.randInt(0, totalGen-1);
            population.get( (pos/cl) )[pos%cl] = Util.randInt(0, 30);
        }
    }
}
