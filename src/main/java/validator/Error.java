package validator;

import lombok.Value;

@Value(staticConstructor = "of")
public class Error {
    String message;
}
