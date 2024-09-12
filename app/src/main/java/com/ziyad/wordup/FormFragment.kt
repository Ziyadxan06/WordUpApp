package com.ziyad.wordup

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import com.google.android.gms.tasks.OnFailureListener
import com.google.android.gms.tasks.OnSuccessListener
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.auth.FirebaseAuth
import com.ziyad.wordup.databinding.FragmentFormBinding

class FormFragment : Fragment() {

    private var _binding: FragmentFormBinding? = null
    private val binding get() = _binding!!
    private lateinit var email : String
    private lateinit var password : String

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentFormBinding.inflate(inflater, container, false)
        return (binding.root)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val activity = requireActivity()
        activity.findViewById<BottomNavigationView>(R.id.bottomNavigation).visibility = View.GONE
        val auth = FirebaseAuth.getInstance()


        binding.btnSignup.setOnClickListener {
            email = binding.emailText.text.toString()
            password = binding.passwordText.text.toString()
            if(email == "" || password == "") {
                Toast.makeText(context, "Enter email and Passeword", Toast.LENGTH_SHORT).show()
            }else{
                auth.createUserWithEmailAndPassword(email, password).addOnSuccessListener {
                    findNavController().navigate(R.id.action_formFragment_to_learningWordsFragment)
                }.addOnFailureListener {
                    Toast.makeText(context, it.localizedMessage, Toast.LENGTH_SHORT).show()
                }
            }
        }

        binding.btnSignin.setOnClickListener {
            email = binding.emailText.text.toString()
            password = binding.passwordText.text.toString()
            if(email == "" || password == "") {
                Toast.makeText(context, "Enter email and Passeword", Toast.LENGTH_SHORT).show()
            }else{
                auth.signInWithEmailAndPassword(email, password).addOnSuccessListener {
                    findNavController().navigate(R.id.action_formFragment_to_learningWordsFragment)
                }.addOnFailureListener {
                    Toast.makeText(context, it.localizedMessage, Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}