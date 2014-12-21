import java.util.concurrent.Callable;

/** numTries should be large */
public class AntCallable implements Callable<Long> {
	private int dimension;
	private int numTries;

	public AntCallable(int dimension, int numTries) {
		this.dimension = dimension;
		this.numTries = numTries;
	}

	@Override
	public Long call() throws Exception {
		// TODO Auto-generated method stub
		long time = 0L;
		for (int i = 0; i < numTries; i++) {
			time += AntWalk.runSimulation(dimension);
		}
		return time;
	}

//	public static void main(String[] args) throws InterruptedException,
//			ExecutionException {
//		int numTries = 1000000;
//		int numThreads = 1000;
//		ExecutorService service = Executors.newFixedThreadPool(numThreads);
//		List<Future<Long>> futures = new ArrayList<Future<Long>>();
//		long start = System.currentTimeMillis();
//		for (int i = 0; i < numThreads; i++) {
//			AntCallable antCallable = new AntCallable(3, numTries);
//			Future<Long> future = service.submit(antCallable);
//			futures.add(future);
//		}
//
//		Long totalTime = 0L;
//		for (Future<Long> future : futures) {
//			totalTime += future.get();
//		}
//		long end = System.currentTimeMillis();
//		System.out.println((double) totalTime / (double) numTries / (double) numThreads);
//		System.out.println(end - start);
//		service.shutdown();
//	}
}
