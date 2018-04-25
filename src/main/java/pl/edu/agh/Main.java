package pl.edu.agh;

import pl.edu.agh.productionmodel.ProductionProcess;
import pl.edu.agh.random.NomiGen;
import pl.edu.agh.random.PoissonGen;

public class Main {

    public static void main(String[] args){
        System.out.println("Say w Szczebrzeszynie chrz¹szcz brzmi w trzcinie! :)");
        //PoissonGen p = new PoissonGen(6666.0);  
        
        ProductionProcess process = new ProductionProcess(400.0, 1000.0, 270.0); //instancja processu z zainicjalizowanymi docelowymi parametrami
        if(args.length == 3)
            process.setTargetParams(Double.parseDouble(args[0]), Double.parseDouble(args[1]), Double.parseDouble(args[2]));
        
        //Ustawaimy zakresy przedzialow losowania
        process.parameters.get(0).setGeneratorRange(60.0);
        process.parameters.get(1).setGeneratorRange(0.05);
        process.parameters.get(2).setGeneratorRange(100.0);
        process.parameters.get(3).setGeneratorRange(0.1);
        process.parameters.get(5).setGeneratorRange(1.0);
        process.parameters.get(6).setGeneratorRange(1.0);
        
        process.setGenerator(new NomiGen());
        
        //process.disableGenerator();
        try{
           Double wjp = process.runProcess(1900.0, 2.0, 16000.0); //uruchom caly proces z domyslnymi wartosciami i zwroc wjp
           System. out.println("Process sucessfull! Obtained WJP = " + wjp);
        }
        catch(Exception e)
        {
            e.printStackTrace();
            System.out.println(e.getMessage());
        }
        
    }
}
