package junseok.snr.heachi02;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

class WalletTest {

    @ValueSource(strings = { "0x9073f43E9bb99C0BB848dFb713eC452C702e7ceD", "0x47567102B073c29621B06cDCaDF9754ed78F4129" })
    @ParameterizedTest
    void getBalanceSuccessTest(String address) throws Exception {
        final Wallet wallet = new Wallet();
        wallet.getBalance(address);
        wallet.getBalanceInEther(address);
    }

    @NullAndEmptySource
    @ParameterizedTest
    void getBalanceAddressIsBlankTest(String address) {
        final Wallet wallet = new Wallet();

        assertThatThrownBy(() -> wallet.getBalance(address))
                .isInstanceOf(BalanceException.class);

    }

    @ValueSource(strings = { "0x9073f43E9bb99C0BB848dFb713eC452C702e7ceF", "0x0" })
    @ParameterizedTest
    void getBalanceInvalidAddressTest(String address) {
        final Wallet wallet = new Wallet();
    }
}