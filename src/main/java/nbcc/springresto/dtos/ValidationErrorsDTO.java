package nbcc.springresto.dtos;

import java.util.List;

public class ValidationErrorsDTO<T> {
    private List<ValidationErrorDTO> errors;

    public T entity;

    public ValidationErrorsDTO() {
    }

    public ValidationErrorsDTO(T entity, List<ValidationErrorDTO> errors) {
        this.entity = entity;
        this.errors = errors;
    }

    public List<ValidationErrorDTO> getErrors() {
        return errors;
    }

    public void setErrors(List<ValidationErrorDTO> errors) {
        this.errors = errors;
    }

    public T getEntity() {
        return entity;
    }

    public void setEntity(T entity) {
        this.entity = entity;
    }
}
