package com.anshu.imageservice.storage.service;

import java.util.UUID;

public interface ImageAccessService {

    String getLatestImageUrl(
            UUID headerUserUuid,
            String headerUserRole,
            UUID deviceUuid
    );
}
