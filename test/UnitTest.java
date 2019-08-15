import org.junit.Test;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.*;

import static org.junit.Assert.assertEquals;

public class UnitTest {
    public static void main(String[] argv) throws FileNotFoundException {
        Scanner sc = new Scanner(new File("./test/test.data"));
        int precision = 10;
        List<Vector<Integer>> data = new ArrayList<>();
        while(sc.hasNext()){
            String s = sc.nextLine();
            String[] sts = s.split(",");
            if(sts.length < 1) continue;
            else{
                Vector<Integer> v = new Vector<>();
                for(int i=0;i<sts.length;i++) v.add((int) (Double.valueOf(sts[i])*precision));
                data.add(v);
            }
        }
        for(Vector<Integer> p: data){
            for(Integer x: p) {System.out.print(x); System.out.print(" ");}
            System.out.println();
        }
        double eps = 1.0;
        double rho = 0.01;
        int dim = 1;
        QuadForest qf = new QuadForest(eps, rho, dim, precision);
        qf.insert(data, false, null);
        Vector<Integer> query = new Vector<>();
        query.add(0);
        int x = qf.query(query);
        System.out.println();
        System.out.println(x);
        //System.out.println(qf.asString());
    }
    @Test
    public void Test() {
        // data size: 10^6
        // eps=2, dim=3, rho=0.002, error_rate:0.2 passed; data: range:-100, 100, random, insert: 11s ~ 10^6
        // eps=0.1, dim=4, rho=0.0005, error_rate:0.0 passed; data: range: -10, 10, random, insert: 8.8s ~ 10^6
        // eps:0.1, dim:4, rho:0.01, error_rate:0.0 passed; data: range:-10, 10, random, insert:6.8s ~ 10^6, query: 0.38ms/dp ~ 480ms/dp(n) ;;  24.7ms~10^7, query: 0.21ms/dp~1744ms/dp(n)
        // dim ++ => precision ++
        // eps: 0.2, dim:4, rho: 0.1, error_rate:0.0 passed; data: range:-10, 10, random, insert:19.3ms ~ 10^7, query: 2.95ms/dp ~ 1707.7ms/dp(n)
        // error_rate<0.1: eps ~ range/0.1k
        // query time: 1.5ms/data point(tree) - 477.8ms/dp(naive) (cond: dim=4, rho=0.0005, eps=0.1, range:-10,10, 10^6p)
        int precise = 10;
        double eps = 0.1;
        int dim = 4;
        double rho = 0.01;
        double error = 0.01;
        QuadForest qf = new QuadForest(eps, rho, dim, precise);
        int tot = (int)5e3;
        List<Vector<Integer>> data = randomGeneration(dim, tot, -4, 4);
        List<Vector<Integer>> query = data.subList(data.size()-100, data.size()-1);
        data = data.subList(0, data.size()-101);
        long st = System.currentTimeMillis();
        qf.insert(data, false, null);
        long ed = System.currentTimeMillis();
        System.out.println("Insert Time: " + (ed-st) + " ms.");
        long time_stat = 0;
        long naive = 0;
        qf.query(query.get(0));
        for(Vector<Integer> q: query){
            long stt = System.currentTimeMillis();
            int ans = qf.query(q);
            long edd = System.currentTimeMillis();
            time_stat += (edd-stt);
            stt = System.currentTimeMillis();
            int res = generateAns(data, q, eps, precise);
            edd = System.currentTimeMillis();
            naive += (edd-stt);
            System.out.println("Expected: " + res + ", Actual:" + ans);
            assertEquals(res*1.0, ans*1.0, res*error);
        }
        double tt = time_stat*1.0/(1.0*query.size());
        double nt = naive*1.0/(1.0*query.size());
        System.out.println("Average Tree Query Cost: " + tt + " ms.");
        System.out.println("Average Naive Query Cost: " + nt + " ms.");
    }
    private Integer generateAns(List<Vector<Integer>> data, Vector<Integer> query, double eps, int precise){
        int cnt = 0;
        for(Vector<Integer> p: data){
            cnt += (ecudlian(p, query, precise) - eps <= 1e-5? 1:0);
        }
        return cnt;
    }
    public List<Vector<Integer>> randomGeneration(int dim, int num, int lower, int upper){
        List<Vector<Integer>> ans = new ArrayList<>();
        while(num!=0){num--;
            Vector<Integer> tmp = new Vector<>();
            for(int i=0;i<dim;i++) tmp.add((int) (Math.random()*(upper-lower) + lower));
            ans.add(tmp);
        }
        return ans;
    }
    public double ecudlian(Vector<Integer> p1, Vector<Integer> p2, int precision){
        double ans = 0;
        for(int i=0;i<p1.size();i++) ans += 1.0*(p1.get(i)-p2.get(i))*(p1.get(i)-p2.get(i))/(1.0*precision*precision);
        return Math.sqrt(ans);
    }
}
