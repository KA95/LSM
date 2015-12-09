import drawing.ConcreteAnalysis;
import org.jzy3d.analysis.AnalysisLauncher;

public class Main {
    public static void main(String[] args) throws Exception {
        AnalysisLauncher.open(new ConcreteAnalysis());
    }

}