package alexis.tecsup.edu.pe.laboratoriocalificado3.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import java.util.List;

import alexis.tecsup.edu.pe.laboratoriocalificado3.R;
import alexis.tecsup.edu.pe.laboratoriocalificado3.adapters.DenunciaAdapter;
import alexis.tecsup.edu.pe.laboratoriocalificado3.models.Denuncia;
import alexis.tecsup.edu.pe.laboratoriocalificado3.services.ApiService;
import alexis.tecsup.edu.pe.laboratoriocalificado3.services.ApiServiceGenerator;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ListaDenuncia extends AppCompatActivity {

    private  static  final  String TAG=ListaDenuncia.class.getSimpleName();

    private RecyclerView denunciasList;

    private static final int REGISTER_FORM_REQUEST=100;
    private static int id_usuario=Bienvenido.The_codigo;
    private static  int tipo_user=Bienvenido.The_tipo;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_lista_denuncia);

        denunciasList = findViewById(R.id.recyclerview);
        denunciasList.setLayoutManager(new LinearLayoutManager(this));

        denunciasList.setAdapter(new DenunciaAdapter());


        initialize();
    }

    private void initialize() {


        ApiService service = ApiServiceGenerator.createService(ApiService.class);
        Call<List<Denuncia>> call = null;

        if (tipo_user==1){
            Log.d( "id1",String.valueOf(id_usuario));


            call = service.getDenuncias();
        }else if(tipo_user==0){
            Log.d( "1d0",String.valueOf(id_usuario));
            call=service.denuncias_x_usuario( id_usuario);

        }




        call.enqueue(new Callback<List<Denuncia>>() {
            @Override
            public void onResponse(Call<List<Denuncia>> call, Response<List<Denuncia>> response) {
                try {

                    int statusCode = response.code();
                    Log.d(TAG, "HTTP status code: " + statusCode);

                    if (response.isSuccessful()) {

                        List<Denuncia> denuncias = response.body();
                        Log.d(TAG, "productos: " + denuncias);

                        DenunciaAdapter adapter = (DenunciaAdapter) denunciasList.getAdapter();
                        adapter.setDenuncias(denuncias);
                        adapter.notifyDataSetChanged();

                    } else {
                        Log.e(TAG, "onError: " + response.errorBody().string());
                        throw new Exception("Error en el servicio");
                    }

                } catch (Throwable t) {
                    try {
                        Log.e(TAG, "onThrowable: " + t.toString(), t);
                        Toast.makeText(ListaDenuncia.this, t.getMessage(), Toast.LENGTH_LONG).show();
                    }catch (Throwable x){}
                }
            }

            @Override
            public void onFailure(Call<List<Denuncia>> call, Throwable t) {
                Log.e(TAG, "onFailure: " + t.toString());
                Toast.makeText(ListaDenuncia.this, t.getMessage(), Toast.LENGTH_LONG).show();
            }

        });
    }

    public void  showRegistrar(View view){
        startActivityForResult(new Intent(this, DenunciaRegister.class), REGISTER_FORM_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode == REGISTER_FORM_REQUEST) {
            // refresh data
            initialize();
        }
    }

}