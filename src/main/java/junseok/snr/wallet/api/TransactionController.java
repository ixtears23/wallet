package junseok.snr.wallet.api;

import junseok.snr.wallet.api.dto.WithdrawDto;
import junseok.snr.wallet.domain.Transaction;
import junseok.snr.wallet.application.service.TransactionService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RequiredArgsConstructor
@RequestMapping("/crypto-wallet/v1/transactions")
@RestController
public class TransactionController {
    private final TransactionService transactionService;

    @ResponseStatus(HttpStatus.OK)
    @GetMapping
    public Page<Transaction> getTransactions(@RequestParam(required = false) Integer startingAfter,
                                             @RequestParam(required = false, defaultValue = "10") Integer size,
                                             @RequestParam(required = false) Integer endingBefore) {
        return transactionService.getTransactionEvents(startingAfter, endingBefore, size);
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping
    public void withdraw(@RequestBody WithdrawDto request) throws Exception {
        log.info(">>>>> withdraw - request : {}", request);
        transactionService.withdraw(request);
    }

}
