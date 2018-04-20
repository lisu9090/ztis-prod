package pl.edu.agh;

import pl.edu.agh.productionmodel.ProductionProcess;

public class Main {

    public static void main(String[] args){
        System.out.println("Say w Szczebrzeszynie chrz¹szcz brzmi w trzcinie! :)");
        
        ProductionProcess process = new ProductionProcess();
        if(args.length == 3)
            process.setTargetParams(Double.parseDouble(args[0]), Double.parseDouble(args[1]), Double.parseDouble(args[2]));
        else
            process.setTargetParams(90.0, 1000.0, 80.0);
        try{
           Double wjp = process.runProcess(1600.0, 2.0, 16000.0);
           System. out.println("Process sucessfull! Obtained WJP = " + wjp);
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
        
    }
}
