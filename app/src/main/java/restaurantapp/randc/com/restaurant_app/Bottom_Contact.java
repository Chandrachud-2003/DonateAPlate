package restaurantapp.randc.com.restaurant_app;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.airbnb.lottie.LottieAnimationView;
import com.balysv.materialripple.MaterialRippleLayout;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.firebase.database.DatabaseReference;
import com.thekhaeng.pushdownanim.PushDownAnim;

public class Bottom_Contact extends BottomSheetDialogFragment {

    private Context mContext;

    private TextView cancelButton;
    private MaterialRippleLayout phoneLayout;
    private MaterialRippleLayout emailLayout;
    private MaterialRippleLayout whatsappLayout;
    private MaterialRippleLayout smsLayout;

    private String email;
    private String phone;


    public Bottom_Contact(Context mContext, String email, String phone) {

        this.mContext = mContext;
        this.email = email;
        this.phone = phone;


    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        setStyle(BottomSheetDialogFragment.STYLE_NORMAL, R.style.CustomBottomSheetDialogTheme_Contacts);

        super.onCreate(savedInstanceState);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.bottom_sheet_contact, container, false);

        findViewsById(v);

        return v;

    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);

    }

    private void findViewsById(View v)
    {
        cancelButton = v.findViewById(R.id.cancelButton);
        phoneLayout = v.findViewById(R.id.phoneRippleLayout);
        whatsappLayout = v.findViewById(R.id.whatsappRippleLayout);
        smsLayout = v.findViewById(R.id.messageRippleLayout);
        emailLayout = v.findViewById(R.id.emailRippleLayout);

        setOnClickListeners();


    }

    private void setOnClickListeners()
    {
        PushDownAnim.setPushDownAnimTo(cancelButton)
                .setScale(PushDownAnim.MODE_SCALE, 0.8f)
                .setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        dismiss();

                    }


                });

        phoneLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                phone.replaceAll(" ","");

                Intent intent = new Intent(Intent.ACTION_DIAL, Uri.fromParts("tel", phone, null));
                startActivity(intent);



            }
        });

        emailLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(Intent.ACTION_VIEW);
                Uri data = Uri.parse("mailto:"+email+"?subject=Food Donation App");
                intent.setData(data);
                startActivity(intent);

            }
        });

        smsLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Uri sms_uri = Uri.parse("smsto:"+phone);
                Intent sms_intent = new Intent(Intent.ACTION_SENDTO, sms_uri);
                startActivity(sms_intent);

            }
        });

        whatsappLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                try {

                    Intent intent = new Intent(Intent.ACTION_VIEW);
                    intent.setData(Uri.parse("http://api.whatsapp.com/send?phone="+phone));
                    startActivity(intent);
                }
                catch (Exception e){
                    e.printStackTrace();
                    Log.d("TAG", "onClick: Error in whatsapp"+e.getMessage());
                }

            }
        });

    }
}

