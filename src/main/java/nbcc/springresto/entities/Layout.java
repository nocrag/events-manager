package nbcc.springresto.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.List;

@Entity
public class Layout {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @NotBlank(message = "Name is required.")
    private String name;

    //@NotBlank(message = "Description is required.")
    private String description;

    @OneToMany(mappedBy = "layout", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<DiningTable> diningTables;

    @CreationTimestamp
    private LocalDateTime createdDate;

    @UpdateTimestamp
    private LocalDateTime updatedDate;

    @Column(name = "is_archived")
    private boolean isArchived;

    public Layout(String name, String description) {
        this.name = name;
        this.description = description;
        this.isArchived = false;
    }

    public Layout() {
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<DiningTable> getDiningTables() {
        return diningTables;
    }

    public void setDiningTable(List<DiningTable> diningTables) {
        this.diningTables = diningTables;
    }


    public LocalDateTime getCreatedDate() {
        return createdDate;
    }

    public boolean getIsArchived() {
        return isArchived;
    }

    public void setArchived(boolean archived) {
        isArchived = archived;
    }


}
