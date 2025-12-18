public abstract class Student {
    protected String id;
    protected String sobrenome;
    protected double mensalidadeAnual;

    public Student(String id, String nome) {
        this.id = id;
        this.sobrenome = nome;
    }

    public abstract void setTuition();
}

public class UndergraduateStudent extends Student {
    public UndergraduateStudent(String id, String nome) { super(id, nome); }
    @Override
    public void setTuition() { this.mensalidadeAnual = 4000 * 2; }
}
// Repetir lógica similar para GraduateStudent ($6000/sem) e StudentAtLarge ($2000/sem).