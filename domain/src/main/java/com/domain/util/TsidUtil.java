package com.domain.util;

import io.hypersistence.tsid.TSID;

public class TsidUtil {

  public static String generate() {
    return TSID.Factory.getTsid().toString();
  }

  public static String encode(long id) {
    return TSID.from(id).toString();
  }

  public static long decode(String id) {
    return TSID.from(id).toLong();
  }

}
