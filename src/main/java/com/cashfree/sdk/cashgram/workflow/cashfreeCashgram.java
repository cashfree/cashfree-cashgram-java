package com.cashfree.sdk.cashgram.workflow;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.logging.Logger;
import java.util.logging.ConsoleHandler;
import java.util.concurrent.ThreadLocalRandom;

import com.cashfree.lib.constants.Constants.Environment;
import com.cashfree.lib.exceptions.IllegalPayloadException;
import com.cashfree.lib.exceptions.ResourceAlreadyExistsException;

import com.cashfree.lib.payout.clients.Payouts;
import com.cashfree.lib.payout.clients.Cashgram;


import com.cashfree.lib.payout.domains.CashgramDetails;


import com.cashfree.lib.logger.VerySimpleFormatter;

public class cashfreeCashgram {
  private static final Logger log = Logger.getLogger(cashfreeCashgram.class.getName());

  static {
    ConsoleHandler consoleHandler = new ConsoleHandler();
    consoleHandler.setFormatter(new VerySimpleFormatter());
    log.addHandler(consoleHandler);
  }

  public static void main(String[] args) {
    Payouts payouts =
        Payouts.getInstance(
            Environment.PRODUCTION,
            "CF1848EZPSGLHWP9IUE2Y",
            "b8df7784dd3f38911294d3597764dd43f3016a48");
    log.info("" + payouts.init());

    boolean isTokenValid = payouts.verifyToken();
    log.info("" + isTokenValid);
    if (!isTokenValid) return;

    log.info("Token is verified");
    Cashgram cashgram = new Cashgram(payouts);

    String cashgramId = "javasdktestcashgram" + ThreadLocalRandom.current().nextInt(0, 1000000);

    CashgramDetails cashgramDetails =
        new CashgramDetails()
            .setCashgramId(cashgramId)
            .setAmount(new BigDecimal("1.00"))
            .setName("John Doe")
            .setEmail("johndoe@cashfree.com")
            .setPhone("9876543210")
            .setLinkExpiry(LocalDateTime.now().plusMinutes(0))
            .setRemarks("api")
            .setNotifyCustomer(1);

        log.info("creating cashgram");
    try {
      log.info("" + cashgram.createCashgram(cashgramDetails));
    } catch (IllegalPayloadException | ResourceAlreadyExistsException x) {
      log.warning(x.getMessage());
    }
    log.info("getting status for the cashgram");
    log.info("" + cashgram.getCashgramStatus(cashgramId));
    log.info("deactivating the cashgram");
    log.info("" + cashgram.deactivateCashgram(cashgramId));
  }
}
