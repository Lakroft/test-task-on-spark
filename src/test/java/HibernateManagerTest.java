import com.lakroft.testtask.model.HibernateManager;
import com.lakroft.testtask.model.User;
import org.junit.Before;
import org.junit.Test;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class HibernateManagerTest {
    private List<User> users;

    @Before
    public void initUsers() {
        users = Arrays.asList(new User("Han", "Solo", 2500),
                new User("Chu", "Baka", 2500),
                new User("Leia", "Organa", 5000));
        HibernateManager.saveAll(users);
    }

    @Test
    public void transferTest() {

        BigDecimal summOnStart = HibernateManager.summ();

        ThreadPoolExecutor executor = (ThreadPoolExecutor) Executors.newFixedThreadPool(2);
        executor.execute(new TransferTask(users.get(0).getId(), users.get(1).getId(), new BigDecimal(10)));
        executor.execute(new TransferTask(users.get(1).getId(), users.get(2).getId(), new BigDecimal(30)));
        executor.execute(new TransferTask(users.get(2).getId(), users.get(0).getId(), new BigDecimal(50)));
        executor.shutdown();
        try {
            executor.awaitTermination(Long.MAX_VALUE, TimeUnit.NANOSECONDS);
        } catch (InterruptedException e) {
            System.out.println("Executor Interrupted");
        }

        List<User> users = HibernateManager.loadAll();
        users.forEach(x -> System.out.printf("- %s%n", x));
        System.out.println("SUMM:" + HibernateManager.summ());
        assert(HibernateManager.summ().equals(summOnStart));
    }

    class TransferTask implements Runnable {
        private long from;
        private long to;
        private BigDecimal amount;

        public TransferTask(long from, long to, BigDecimal amount) {
            this.from = from;
            this.to = to;
            this.amount = amount;
        }

        @Override
        public void run() {
            for (int i = 0; i < 5; i++) {
                System.out.println(HibernateManager.transfer(from, to, amount));
                try {
                    TimeUnit.MILLISECONDS.sleep(300);
                }catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
