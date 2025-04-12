package com.domain.util;

import io.hypersistence.tsid.TSID;

public class TsidUtil {

  public static Long generate() {
    return decode(TSID.Factory.getTsid().toString());
  }

  public static String encode(long id) {
    return TSID.from(id).toString();
  }

  public static long decode(String id) {
    return TSID.from(id).toLong();
  }

}
