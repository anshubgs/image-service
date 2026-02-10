package com.anshu.imageservice.service;

import java.util.UUID;

public interface StorageService {

    /**
     * @return stored object path or identifier (not public URL)
     */
  //  String store(byte[] imageBytes, String imageUuid);

	String store(byte[] imageBytes, UUID deviceUuid, UUID imageUuid);
}
