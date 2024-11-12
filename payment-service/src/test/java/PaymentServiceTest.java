import com.example.PaymentMain;
import com.example.enums.PaymentStatus;
import com.example.enums.PaymentType;
import com.example.model.Item;
import com.example.model.Transaction;
import com.example.services.PaymentService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.kafka.test.context.EmbeddedKafka;
import org.springframework.test.annotation.DirtiesContext;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest(classes = PaymentMain.class)
@EmbeddedKafka(partitions = 1, topics = { "payment_events" })
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class PaymentServiceTest {

    @Autowired
    private PaymentService paymentService;
    

    @Test
    void testTransactionLifecycle() {
        // Création de la Transaction
        Item item = Item.builder().name("T-shirt").price(new BigDecimal("19.99")).quantity(5).build();
        Transaction transaction = Transaction.builder()
                .totalAmount(new BigDecimal("99.95"))
                .paymentType(PaymentType.CREDIT_CARD)
                .items(List.of(item))
                .build();

        transaction = paymentService.createTransaction(transaction);

        // Autorisation de la Transaction
        transaction = paymentService.updateTransactionStatus(transaction.getId(), PaymentStatus.AUTHORIZED);

        // Capture de la Transaction
        transaction = paymentService.updateTransactionStatus(transaction.getId(), PaymentStatus.CAPTURED);

        // Assertions pour vérifier le statut final
        assertEquals(PaymentStatus.CAPTURED, transaction.getPaymentStatus());
    }
}
