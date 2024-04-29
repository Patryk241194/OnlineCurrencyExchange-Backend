package com.kodilla.onlinecurrencyexchangebackend.service.email;

import lombok.*;

@Getter
@Builder
@RequiredArgsConstructor
public class Mail {

    private final String mailTo;
    private final String ToCc;
    private final String subject;
    private final String message;

}
