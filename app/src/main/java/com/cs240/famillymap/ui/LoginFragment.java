package com.cs240.famillymap.ui;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.EventLog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.cs240.famillymap.R;
import com.cs240.famillymap.model.DataCache;
import com.cs240.famillymap.model.Helper;
import com.cs240.famillymap.net.ServerProxy;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.TreeMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import model.Event;
import model.Person;
import request.AuthRequest;
import request.EventRequest;
import request.LoginRequest;
import request.RegisterRequest;
import result.EventResult;
import result.EventsResult;
import result.LoginResult;
import result.PersonsResult;
import result.RegisterResult;

/***
 * Handle a User logging in.
 */
public class LoginFragment extends Fragment {

    private View rootView;
    private EditText serverHost;
    private EditText serverPort;
    private EditText username;
    private EditText password;
    private EditText firstName;
    private EditText lastName;
    private EditText email;

    //String -> Set with listeners.
    private String serverHostStr = "";
    private String serverPortStr = "";
    private String usernameStr = "";
    private String passwordStr = "";
    private String firstNameStr = "";
    private String lastNameStr = "";
    private String emailStr = "";

    private RadioGroup gender;
    private Button signIn;
    private Button register;



    public LoginFragment() {
        // Required empty public constructor
    }



    public static LoginFragment newInstance() {
        LoginFragment fragment = new LoginFragment();
        return fragment;
    }




    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootView = inflater.inflate(R.layout.fragment_login, container, false);

        //Setup Pointers
        serverHost = rootView.findViewById(R.id.server_host);
        serverPort = rootView.findViewById(R.id.server_port);
        username = rootView.findViewById(R.id.username);
        password = rootView.findViewById(R.id.password);
        firstName = rootView.findViewById(R.id.first_name);
        lastName = rootView.findViewById(R.id.last_name);
        email = rootView.findViewById(R.id.email);
        signIn = rootView.findViewById(R.id.sign_in);
        register = rootView.findViewById(R.id.register);
        gender = rootView.findViewById(R.id.gender);

        //Setup SignIn listener
        signIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signIn();
            }
        });

        //Setup Register Listener
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                register();
            }
        });

        enableButtons();

        //Listeners: If there is text enable or disable the Login or register buttons:

        //Host listener
        serverHost.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                serverHostStr = s.toString();
                enableButtons();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        //Port listener
        serverPort.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                serverPortStr = s.toString();
                enableButtons();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        //Username listener
        username.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                usernameStr = s.toString();
                enableButtons();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        //Password listener
        password.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                passwordStr = s.toString();
                enableButtons();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        //Firstname listener
        firstName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                firstNameStr = s.toString();
                enableButtons();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        //Lastname listener
        lastName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                lastNameStr = s.toString();
                enableButtons();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        //Email listener
        email.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                emailStr = s.toString();
                enableButtons();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        return rootView;
    }



    //If the user has filled all text, enable the login and register buttons.
    //If the user has filled in the host, port, username, and password, then enable login.
    //Otherwise, they are both disabled.
    private void enableButtons(){
        if(serverHostStr.length() != 0 && serverPortStr.length() != 0 &&
            usernameStr.length() != 0 && passwordStr.length() != 0 &&
            firstNameStr.length() != 0 && lastNameStr.length() != 0 &&
            emailStr.length() != 0){

            signIn.setEnabled(true);
            register.setEnabled(true);
        }
        else if(serverHostStr.length() != 0 && serverPortStr.length() != 0 &&
                usernameStr.length() != 0 && passwordStr.length() != 0){
            signIn.setEnabled(true);
            register.setEnabled(false);
        }
        else{
            signIn.setEnabled(false);
            register.setEnabled(false);
        }
    }



    /***
     * Create a New Thread. If the user successfully logs in, then call main activity to
     * swap the map fragments. Otherwise, let the user know the error.
     */
    private void signIn(){
        proxySetup();
        Handler uiThreadMessageHandler = new Handler(Looper.getMainLooper()){
            @Override
            public void handleMessage(Message message){
                Bundle bundle = message.getData();
                if(bundle.getBoolean("Outcome")){
                    ((MainActivity) getActivity()).login();
                }
                else{
                    Toast.makeText(requireActivity().getApplicationContext(),
                            bundle.getString("Message"), Toast.LENGTH_SHORT).show();
                }
            }
        };

        LoginTask task = new LoginTask(uiThreadMessageHandler,getSignIn());
        ExecutorService executor = Executors.newSingleThreadExecutor();
        executor.submit(task);
    }



    /***
     * Create a New Thread. If the user successfully registers in, then call main activity to
     * swap the map fragments. Otherwise, let the user know the error.
     */
    public void register(){
        proxySetup();
        Handler uiThreadMessageHandler = new Handler(Looper.getMainLooper()){
            @Override
            public void handleMessage(Message message){
                Bundle bundle = message.getData();
                if(bundle.getBoolean("Outcome")){
                    ((MainActivity) getActivity()).login();
                }
                else{
                    Toast.makeText(requireActivity().getApplicationContext(),
                            bundle.getString("Message"), Toast.LENGTH_SHORT).show();
                }
            }
        };

        LoginTask task = new LoginTask(uiThreadMessageHandler,getRegister());
        ExecutorService executor = Executors.newSingleThreadExecutor();
        executor.submit(task);
    }



    /***
     * Setup the server Proxy with the user input for host and server.
     */
    private void proxySetup(){
        //Setup Server Proxy
        ServerProxy sp = ServerProxy.getInstance();
        sp.setHostname(serverHost.getText().toString());
        sp.setPort(Integer.parseInt(serverPort.getText().toString()));
    }



    /***
     * Create a login request object from the user input.
     * @return - The new LoginRequest object.
     */
    private LoginRequest getSignIn(){
        //New Login Object
        return new LoginRequest(username.getText().toString(), password.getText().toString());
    }



    /***
     * Create a register request object from the user input.
     * @return - The new RegisterRequest object.
     */
    private RegisterRequest getRegister(){
        String sex;

        //Determine Gender
        if(gender.getCheckedRadioButtonId() == R.id.male){
            sex = "m";
        }
        else{
            sex ="f";
        }

        //New Register Request Object
        RegisterRequest request =
                new RegisterRequest(username.getText().toString(),password.getText().toString(),
                        email.getText().toString(),firstName.getText().toString(),
                        lastName.getText().toString(),sex);
        return request;
    }



    /***
     * The background thread. It will attempt to login/register. If successful, it will
     * use the server proxy to load the dataCache with events and persons relevant
     * to the user.
     */
    private static class LoginTask implements Runnable{

        private final Handler messageHandler;
        private final LoginRequest loginRequest;
        private final RegisterRequest registerRequest;
        private AuthRequest aRequest;
        private String personID;
        private DataCache cache;



        /***
         * LoginTask Constructor
         * @param messageHandler - The handler to report the outcome.
         * @param loginRequest - The LoginRequest.
         */
        public LoginTask(Handler messageHandler, LoginRequest loginRequest){
            this.messageHandler = messageHandler;
            this.registerRequest = null;
            this.loginRequest = loginRequest;
        }



        /***
         * RegisterRequest Handler
         * @param messageHandler - The handler to report the outcome.
         * @param registerRequest - The RegisterRequest.
         */
        public LoginTask(Handler messageHandler, RegisterRequest registerRequest){
            this.messageHandler = messageHandler;
            this.registerRequest = registerRequest;
            this.loginRequest = null;
        }



        /***
         * The Single run method. Attempts to login/register.
         * If successful, it will load the data.
         */
        @Override
        public void run() {
            ServerProxy proxy = ServerProxy.getInstance();
            cache = DataCache.getInstance();
            //Login Request
            if(loginRequest != null){
                LoginResult result = proxy.login(loginRequest);
                if(result.isSuccess()){
                    personID = result.getPersonID();
                    aRequest = new AuthRequest(result.getAuthtoken());
                    loadData();
                }
                else{
                    sendMessage("Error: Failed to login", false);
                }
            }
            //Register Request
            else if(registerRequest != null){
                RegisterResult result = proxy.register(registerRequest);
                if(result.isSuccess()){
                    personID = result.getPersonID();
                    aRequest = new AuthRequest(result.getAuthtoken());
                    loadData();
                }
                else{
                    sendMessage("Error: Failed to register", false);
                }
            }
        }



        /***
         * Send the message to the handler.
         *
         * @param m - The message to send.
         * @param o - boolean for outcome of the event.
         */
        private void sendMessage(String  m, Boolean o){
            Message message = Message.obtain();
            Bundle msgBundle = new Bundle();
            msgBundle.putString("Message",m);
            msgBundle.putBoolean("Outcome",o);
            message.setData(msgBundle);
            messageHandler.sendMessage(message);
        }



        /***
         * Load event and personData from the proxy. Then precompute: paternal, maternal,
         * male, female, and get filter events ready to filter. The code create 2 deep copies
         * for the filter data because if I don't and call clear() in my filter function,
         * It destroys my original data set.
         */
        private void loadData(){
            ServerProxy proxy = ServerProxy.getInstance();
            EventsResult eResult = proxy.events(aRequest);
            PersonsResult pResult = proxy.persons(aRequest);

            Map<String, Person> peopleMap = new HashMap<>();
            Map<String, Event> eventsMap = new HashMap<>();
            Map<String, List<Event>> events = new HashMap<>();
            ArrayList<Event> personEvents = new ArrayList<>();
            Map<String, Float> eventColor = new HashMap<>();

            Map<String, Person> male = new HashMap<>();
            Map<String, Person> female = new HashMap<>();

            Map<String, Person> filterPeopleMap = new HashMap<>();
            Map<String, Event> filterEventsMap = new HashMap<>();
            Map<String, List<Event>> filterEvents = new HashMap<>();
            ArrayList<Event> filterPersonEvents = new ArrayList<>();


            //Set PersonId for current User
            cache.setPersonID(personID);

            //Set Person Map
            for(Person p: pResult.getData()){
                peopleMap.put(p.getPersonID(),p);
                filterPeopleMap.put(p.getPersonID(),p);
                if(p.getGender().equals("m")){
                    male.put(p.getPersonID(),p);
                }
                else if(p.getGender().equals("f")){
                    female.put(p.getPersonID(),p);
                }
            }
            cache.setPeopleMap(peopleMap);
            cache.setFilterPeople(filterPeopleMap);
            cache.setMale(male);
            cache.setFemale(female);

            //Set Event Map
            for(Event e: eResult.getData()){
                e.setEventType(e.getEventType().toUpperCase());
                eventsMap.put(e.getEventID(),e);
                filterEventsMap.put(e.getEventID(),e);
            }
            cache.setEventsMap(eventsMap);
            cache.setFilterEventMap(filterEventsMap);

            int index = 0;
            //Set Event Colors
            for(Event e: eResult.getData()){
                if(eventColor.get(e.getEventType().toUpperCase()) == null){
                    //Recycle Colors
                    if(index >= cache.getColors().size()){
                        index = 0;
                    }
                    eventColor.put(e.getEventType().toUpperCase(),cache.getColors().get(index));
                    index++;
                }
            }
            cache.seteventColors(eventColor);

            //Set map for Each Person that contains their events
            for(Person p: pResult.getData()){
                personEvents.clear();
                filterPersonEvents.clear();
                //For each event
                for(Event e: eResult.getData()){
                    if(p.getPersonID().equals(e.getPersonID())){
                        personEvents.add(e);
                        filterPersonEvents.add(e);
                    }
                }
                Helper helper = new Helper();
                filterEvents.put(p.getPersonID(),helper.sortYear(personEvents));
                events.put(p.getPersonID(),helper.sortYear(personEvents));
            }
            cache.setEvents(events);
            cache.setFilterEvents(filterEvents);

            getPaternal();
            getMaternal();
            sendMessage("Success", true);
        }



        /***
         * If there is a father, call ancestors to recursively add paternal.
         */
        private void getPaternal(){
            Map<String, Person> paternal = new HashMap<>();
            String fatherID = cache.getPeopleMap().get(cache.getPersonID()).getFatherID();
            if(!fatherID.equals(null)){
                ancestors(paternal,fatherID);
            }
            cache.setPaternal(paternal);
        }



        /***
         * If there is a mother, call ancestors to recursively add ,maternal.
         */
        private void getMaternal(){
            Map<String, Person> maternal = new HashMap<>();
            String motherID = cache.getPeopleMap().get(cache.getPersonID()).getMotherID();
            if(!motherID.equals(null)){
                ancestors(maternal,motherID);
            }
            cache.setMaternal(maternal);
        }



        /***
         * Recursive function. Takes a map of ancestors and adds the current person to it.
         * If there are additional fathers or mothers to add, it will recursively add them.
         *
         * @param ancestors - The map to add to.
         * @param personID - The current ancestor to add and check for further ancestors.
         */
        private void ancestors(Map<String, Person> ancestors, String personID){

            Person person = cache.getPeopleMap().get(personID);

            ancestors.put(personID,person);
            String fatherID = person.getFatherID();
            String motherID = person.getMotherID();

            if(fatherID != null){
                ancestors(ancestors,fatherID);
            }
            if(motherID != null){
                ancestors(ancestors,motherID);
            }
        }
    };
}
