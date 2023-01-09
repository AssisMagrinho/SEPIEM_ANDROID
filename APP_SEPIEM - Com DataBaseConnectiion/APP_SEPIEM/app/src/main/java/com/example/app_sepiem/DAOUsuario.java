package com.example.app_sepiem;

import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class DAOUsuario {

    private DatabaseReference databaseReference;

    public DAOUsuario() {

        FirebaseDatabase db = FirebaseDatabase.getInstance();
        databaseReference = db.getReference(Inscrito.class.getSimpleName());
    }

   public Task<Void> add(Inscrito Usu){

        return databaseReference.push().setValue(Usu);
    }
}
