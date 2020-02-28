package com.nidotim.lottery.scheduler;

import java.time.Instant;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

@Slf4j
@NoArgsConstructor
public abstract class BaseCleanUpScheduler<T> {

  protected boolean enabled = false;

  protected int expiryTime = 86400; // one day

  private int pageSize = 200;

  private boolean batchCleanUp;

  public BaseCleanUpScheduler(boolean enabled, int expiryTime, int pageSize, boolean batchCleanUp) {
    this.enabled = enabled;
    this.expiryTime = expiryTime;
    this.pageSize = pageSize;
    this.batchCleanUp = batchCleanUp;
  }

  protected void schedulerFired() {
    if (!enabled) {
      return;
    }

    Instant expiredDate = Instant.now().minusSeconds(expiryTime);

    Pageable pageable = PageRequest.of(0, pageSize);
    Page<T> expiredEntityPage;

    // used to prevent infinite loop if deleting fails
    int prevTotalElements = 0;

    do {
      expiredEntityPage = findExpired(expiredDate, pageable);
      if (expiredEntityPage.isEmpty()
          || expiredEntityPage.getTotalElements() == prevTotalElements) {
        return;
      }
      prevTotalElements = expiredEntityPage.getTotalPages();

      if (batchCleanUp) {
        try {
          batchCleanUp(expiredEntityPage);
        } catch (Exception e) {
          // ignore
          log.error(getBatchErrorMessage(expiredEntityPage) + ", errorMsg:" + e.getMessage());
        }
      } else {
        for (T t : expiredEntityPage.getContent()) {
          try {
            cleanUp(t);
          } catch (Exception e) {
            // ignore
            log.error(getErrorMessage(t) + ", errorMsg:" + e.getMessage());
          }
        }
      }
    } while (expiredEntityPage.hasNext());
  }

  abstract Page<T> findExpired(Instant expiryDate, Pageable pageable);

  abstract void cleanUp(T entity);

  abstract void batchCleanUp(Page<T> entityPage);

  abstract String getErrorMessage(T entity);

  abstract String getBatchErrorMessage(Page<T> entityPage);
}
