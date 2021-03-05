package com.phoenix.core.bussiness.auth;

import com.phoenix.common.mfa.MfaUtils;
import com.phoenix.common.mfa.data.HashingAlgorithm;
import com.phoenix.common.mfa.data.MfaDataFactory;
import com.phoenix.common.mfa.data.MfaType;
import com.phoenix.common.mfa.data.TotpData;
import com.phoenix.common.mfa.generator.QrGenerator;
import com.phoenix.common.mfa.generator.ZxingPngQrGenerator;
import com.phoenix.common.security.KeyProvider;
import com.phoenix.common.security.TokenProvider;
import com.phoenix.common.util.Base32;
import com.phoenix.core.bussiness.UseCase;
import com.phoenix.core.common.UseCaseResponse;
import com.phoenix.core.common.UseCaseStatus;

import java.security.SecureRandom;
import java.util.Arrays;

public class CreateQrCodeForSignIn implements UseCase<String, String> {
    private final KeyProvider keyProvider;
    private final TokenProvider tokenProvider;

    public CreateQrCodeForSignIn(KeyProvider keyProvider, TokenProvider tokenProvider) {
        this.keyProvider = keyProvider;
        this.tokenProvider = tokenProvider;
    }

    @Override
    public void validate(String s) {

    }

    @Override
    public UseCaseResponse<String> execute(String token) {
        try {
            SecureRandom secureRandom = new SecureRandom();
            byte[] bytes = new byte[(32 * 5) / 8];
            secureRandom.nextBytes(bytes);

            String key = Base32.encode(bytes);

            System.out.println(key);

            //String key = keyProvider.getKeyWrapper().getEncoded();
            String username = (String) tokenProvider.getClaimsFromToken(token).get("username");

            TotpData data = (TotpData) MfaDataFactory.getMfaData(MfaType.TOTP, username, key,
                    "AppName", HashingAlgorithm.SHA1, 6, 30);

            QrGenerator generator = new ZxingPngQrGenerator();
            byte[] imageData = generator.generate(data);

            String mimeType = generator.getImageMimeType();

            String dataUri = MfaUtils.getDataUriForImage(imageData, mimeType);

            return new UseCaseResponse<>(UseCaseStatus.SUCCESS, "", dataUri);
        } catch (Exception e) {
            return new UseCaseResponse<>(UseCaseStatus.FAILED, e.getMessage(), null);
        }
    }
}
