package top.agno.gnosis.utils.http;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RequestResult {

    private int statusCode;

    private String Result;

    private String message;
}
