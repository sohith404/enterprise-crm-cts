package com.crm.entities;

import com.crm.enums.Status;
import com.crm.enums.Type;
import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Notification {
    @Enumerated(EnumType.STRING)
    private Type type;
    @Enumerated(EnumType.STRING)
    private Status status;
    private String subject;
    private String body;
    private String emailFor;
}