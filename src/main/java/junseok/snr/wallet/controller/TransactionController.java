package junseok.snr.wallet.controller;

import junseok.snr.wallet.service.TransactionService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RequestMapping("/crypto-wallet/v1/transactions")
@RestController
public class TransactionController {
    private final TransactionService transactionService;

}
