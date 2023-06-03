package junseok.snr.wallet.api.controller;

import junseok.snr.wallet.api.controller.dto.WithdrawDto;
import junseok.snr.wallet.api.domain.Transaction;
import junseok.snr.wallet.api.service.TransactionService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@RequestMapping("/crypto-wallet/v1/transactions")
@RestController
public class TransactionController {
    private final TransactionService transactionService;

    @ResponseStatus(HttpStatus.OK)
    @GetMapping
    public List<Transaction> getTransactions(@RequestParam(required = false) String startingAfter,
                                             @RequestParam(required = false) String endingBefore) {
        return transactionService.getTransactions(startingAfter, endingBefore);
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping
    public void withdraw(@RequestBody WithdrawDto request) throws Exception {
        log.info(">>>>> withdraw - request : {}", request);
        transactionService.withdraw(request);
    }

}
