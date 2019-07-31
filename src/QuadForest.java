import java.io.Serializable;
import java.util.*;

public class QuadForest implements Serializable {
    private Map<Vector<Integer>, QuadTree> forest;
    private double eps;
    private double rho;
    private int dim;
    private int precise;
    public QuadForest(double eps, double rho, int dim, int precise){
        this.eps = eps; this.rho = rho; this.dim = dim; this.precise = precise;
        forest = new HashMap<>();
    }
    public void insert(List<Vector<Integer>> points){
        Map<Vector<Integer>, List<Vector<Integer>>> append = new HashMap<>();
        for(Vector<Integer> p: points){
            Vector<Integer> base = new Vector<>();
            for (Integer integer: p) base.add((int) ((integer * 1.0) / (precise*1.0 * (eps / Math.sqrt(dim)))));
            //System.out.println("insert");
            //for(Integer c: base) System.out.print(c); System.out.print(" ");
            //System.out.println();
            if(forest.containsKey(base)){
                if(append.containsKey(base)) append.get(base).add(p);
                else{
                    List<Vector<Integer>> tmp = new ArrayList<>();
                    tmp.add(p);
                    append.put(base, tmp);
                }
            }
            else {
                QuadTree qt = new QuadTree(base, eps, rho, precise);
                forest.put(base, qt);
                List<Vector<Integer>> tmp = new ArrayList<>();
                tmp.add(p);
                append.put(base, tmp);
            }
        }
        for(Vector<Integer> base: append.keySet()){
            forest.get(base).insert(append.get(base));
        }
    }
    public void delete(List<Vector<Integer>> points){
        Map<Vector<Integer>, List<Vector<Integer>>> append = new HashMap<>();
        for(Vector<Integer> p: points){
            Vector<Integer> base = new Vector<>();
            for (Integer integer: p) base.add((int) ((integer * 1.0) / (precise * (eps / Math.sqrt(dim)))));
            //for(Integer c: base) System.out.print(c); System.out.print(" ");
            //System.out.println();
            if(forest.containsKey(base)){
                if(append.containsKey(base)) append.get(base).add(p);
                else{
                    List<Vector<Integer>> tmp = new ArrayList<>();
                    tmp.add(p);
                    append.put(base, tmp);
                }
            }
            else {
                System.out.println("[E] Delete with non-exist QuadTree->Node->Point.");
                System.out.println("[I] Continue deletion, skip error. [POS: QuadForest:delete].");
            }
        }
        for(Vector<Integer> base: append.keySet()){
            forest.get(base).delete(append.get(base));
        }
    }
    public int query(Vector<Integer> p){
        int ans = 0;
        Vector<Integer> cell = new Vector<>();
        for(Integer x: p) cell.add((int) (x*1.0/(precise*1.0*(eps/Math.sqrt(dim)))));
        //for(Integer c: cell) System.out.print(c); System.out.print(" ");
        //System.out.println();
        Vector<Double> point = new Vector<>();
        for(Integer x: p) point.add((x*1.0)/(precise*1.0));
        List<Vector<Integer>> cellList = generatePossibleCells(cell);
        for(int i=0;i<cellList.size();i++){
            double dis = eculdian_cells(cellList.get(i), p);
            double r2 = Math.sqrt(Math.pow(eps/(2*Math.sqrt(dim)), 2)*dim);
            if(dis + r2 - eps <= 1e-5){
                //System.out.print(i);
                if(forest.containsKey(cellList.get(i))) ans += forest.get(cellList.get(i)).query(point);
            }
            else if(dis - r2 - eps <= 1e-5){
                //System.out.print(i);
                if(forest.containsKey(cellList.get(i))) ans += forest.get(cellList.get(i)).query(point);
            }
        }

        return ans;
    }
    private List<Vector<Integer>> generatePossibleCells(Vector<Integer> cell){
        int rad = (int) (Math.sqrt(dim)+0.5);
        //System.out.println(rad);
        List<Vector<Integer>> ans = new ArrayList<>();
        ans.add(new Vector<>());
        for(int i=0;i<dim;i++){
            List<Vector<Integer>> tmp = new ArrayList<>();
            for(int j=-rad;j<=rad;j++){
                for(Vector<Integer> p: ans){
                    Vector<Integer> x = (Vector<Integer>) p.clone();
                    x.add(j);
                    tmp.add(x);
                }
            }
            ans = tmp;
        }
        //System.out.println(ans.size());
        //for(int i=0;i<ans.size();i++) System.out.println(ans.get(i).get(0));
        List<Vector<Integer>> finalist = new ArrayList<>();
        for(Vector<Integer> p: ans){
            Vector<Integer> tmp = new Vector<>();
            for(int i=0;i<p.size();i++) tmp.add(p.get(i)+cell.get(i));
            finalist.add(tmp);
        }
        return finalist;
    }
    private double eculdian_cells(Vector<Integer> cell1, Vector<Integer> p){
        double ans = 0;
        for(int i=0;i<cell1.size();i++){
            ans += (cell1.get(i)*eps/Math.sqrt(dim)-p.get(i)/(1.0*precise))*(cell1.get(i)*eps/Math.sqrt(dim)-p.get(i)/(1.0*precise));
        }
        //System.out.println(ans);
        return Math.sqrt(ans);
    }
    public String asString(){
        StringBuilder s = new StringBuilder();
        for(Vector<Integer> k: forest.keySet()) s.append(forest.get(k).asString());
        return s.toString();
    }
}
