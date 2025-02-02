package earlybird.earlybird.common;

import jakarta.annotation.PostConstruct;
import java.util.TimeZone;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DefaultTimeZoneConfig {

  @PostConstruct
  public void setDefaultTimeZoneToKST() {
    TimeZone.setDefault(TimeZone.getTimeZone("Asia/Seoul"));
  }
}
