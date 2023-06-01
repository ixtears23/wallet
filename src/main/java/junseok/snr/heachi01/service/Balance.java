package junseok.snr.heachi01.service;

import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.DefaultBlockParameterName;
import org.web3j.protocol.core.methods.response.EthGetBalance;
import org.web3j.protocol.http.HttpService;

import java.math.BigInteger;

public class Balance {
    private final Web3j web3j;
    public Balance() {
        this.web3j = Web3j.build(new HttpService("https://tn.henesis.io/ethereum/goerli"));
    }

    public BigInteger balance(final String address) throws Exception {
        EthGetBalance ethGetBalance = web3j.ethGetBalance(address, DefaultBlockParameterName.LATEST)
                .sendAsync()
                .get();

        return ethGetBalance.getBalance();
    }
}
