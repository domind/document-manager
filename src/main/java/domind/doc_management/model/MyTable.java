package domind.doc_management.model;

import lombok.Data;

import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import javax.persistence.*;


@Data
@NoArgsConstructor
@RequiredArgsConstructor
@Entity
@Table(name = "my_table")
public class MyTable {

    @Id
    @GeneratedValue
    private Long id;
    @NonNull
    private String category;
    private String description;
    private java.sql.Date issueDate;
    private java.sql.Date expiryDate;
    private Boolean myCheckBox;
    private String filePath;

    public Long getId() {
        return id;
    }

    public Boolean getMyCheckBox() {
        return myCheckBox;
    }

    public String getCategory() {
        return category;
    }
    public String getDescription() {
        return description;
    }
    public java.sql.Date getIssueDate() {
        return issueDate;
    }
    public java.sql.Date getExpiryDate() {
        return expiryDate;
    }
    

    public String getFilePath() {
        return filePath;
    }


    public void setCategory(String category) {
        this.category = category;
    }
}
