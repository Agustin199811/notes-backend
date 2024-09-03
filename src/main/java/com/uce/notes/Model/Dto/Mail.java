package com.uce.notes.Model.Dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class Mail {
    private String toUser;
    private String subject;
    private String message;

}
