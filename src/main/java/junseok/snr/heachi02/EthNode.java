package junseok.snr.heachi02;

import lombok.Getter;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.http.HttpService;

public class EthNode {
    private static final String ETH_NODE_ENDPOINT = "https://tn.henesis.io/ethereum/goerli?clientId=815fcd01324b8f75818a755a72557750";
    @Getter
    private static final Web3j web3j = Web3j.build(new HttpService(ETH_NODE_ENDPOINT)); // 이더리움 노드 엔드포인트

}
