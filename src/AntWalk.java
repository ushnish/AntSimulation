import java.util.concurrent.ThreadLocalRandom;

/* Idea is that the cube vertices are modelled as 3-digit binary numbers and an edge between
 * two vertices exists if they differ in exactly 1 digit. Therefore the next step is taken by 
 * generating random digit from 0 to 2 and xor-ing 1 to that position. From a random start point 
 * the end position is determined by xor-ing 111. 
 */
public class AntWalk {
	public static long runSimulation(int dimension) {
		int start = ThreadLocalRandom.current().nextInt(1 << dimension);
		int end = start^((1 << dimension) - 1);
		long time = 1L;		
		int rand = ThreadLocalRandom.current().nextInt(dimension);
		int next = start^(1 << rand);
		while (next != end) {
			time++;
			rand = ThreadLocalRandom.current().nextInt(dimension);
			next = next^(1 << rand);
		}
		return time;
	}
	
	
//	public static void main(String[] args) {
//		int num = 100000000;
//		long total = 0;
//		Random generator = new Random();
//		long start = System.currentTimeMillis();
//		for (int i = 0; i < num ; i++) {
//			total += runSimulation(3, generator);
//		}
//		long end = System.currentTimeMillis();
//		System.out.println((double)total/(double)num);
//		System.out.println(end - start);
//	}
	
}
