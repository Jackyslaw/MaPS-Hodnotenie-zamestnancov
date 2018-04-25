<template>
        <div class="login-main-div">
            <span class="login-error">{{ error }}</span>
            <form class="col s6" @submit.prevent="login">
                <div class="row">
                    <div>
                        <div  class="input-field col s12">
                            <label for="login-username">Email</label>
                            <input id="login-username" type="email" class="validate" required="true" v-model="username">
                        </div>
                    </div>
                        <div class="input-field col s12">
                            <label for="login-password">Password</label>
                            <input id="login-password" type="password" class="validate" v-model="password">
                        </div>
                    <div>
                        <button class="btn waves-effect waves-light t-systems-btn login-btn" type="submit">Login
                            <i class="material-icons right"></i>
                        </button>
                    </div>
                </div>
            </form>
        </div>
</template>
<script>
    import ajax from '../ajax';
    import { jwt } from '../utils';
    import Lockr from '../lockr';
    Lockr.prefix = 'fibimeter_';

    export default {
        name: 'app-login',
        data: () => ({
            username: '',
            password: '',
            error: '',
        }),
        methods: {
            login() {
                const self = this;
                // data:
                const credentials = {
                    username: self.username,
                    password: self.password,
                };

                // ******* HELPERS FUNCTIONS ******* //
                const processSuccessfulResponse = ({ data, headers }) => {
                    Lockr.set('jwt', headers.authorization);
                    self.$eventBus.$emit('login');
                    self.$router.replace('/fibimeter');
                }

                const processErrorResponse = ({ code }) => {
                    if(code === 401) {
                        self.error = 'Invalid login attempt. Please enter your valid credentials.';
                    }
                }

                const makeRequest = ({ username, password }) => {
                    return new Promise((resolve, reject) => {
                        ajax.post(
                            '/login',
                            {
                                username,
                                password,
                            },
                            (res, err) => {
                                if(!err) {
                                    return resolve(res);
                                }
                                reject(err);
                            },
                            true,
                            ['authorization'],
                        )
                    });
                }

                const validate = ({ username, password }) => {
                    // If some client-side validation is required, here is the place:
                    return true;
                }


                // ******** ACTUAL LOGIC ******** //
                self.error = '';
                if(validate(credentials)) {
                    makeRequest(credentials)
                    .then(processSuccessfulResponse)
                    .catch(processErrorResponse);
                }
            },

            checkIfLoggedIn() {
                const token = Lockr.get('jwt', false);
                if(token) {
                    this.$router.replace('/fibimeter');
                }
            }
        },
        mounted() {
            let self = this;
            this.$nextTick(() => {
                self.checkIfLoggedIn();
            });
        },
    }
</script>
<style>
.login-main-div {
    margin: 10% auto;
    max-width: 50%;
    padding: 30px 50px 0px 50px;
    background-color: rgba(212, 82, 149, 0.36);
    border-radius: 20px;
    border: 1px solid rgba(125, 125, 125, 0.55);
}

.login-main-div .btn.waves-effect.waves-light.login-btn {
  float: right;
}

/* label color */
.login-main-div .input-field label {
  color: rgba(190,190,190,0.44);
}
/* label focus color */
.login-main-div .input-field input[type=email]:focus + label, .login-main-div .input-field input[type=password]:focus + label {
  color: rgb(226,0,116);
}
/* label underline focus color */
.login-main-div .input-field input[type=email]:focus, .login-main-div .input-field input[type=password]:focus {
  border-bottom: 1px solid rgb(226,0,116);
  box-shadow: 0 1px 0 0 rgb(226,0,116);
}
/* valid color */
.login-main-div .input-field input[type=email].valid:not(:focus), .login-main-div .input-field input[type=password].valid:not(:focus) {
  border-bottom: 1px solid #9e9e9e;
  box-shadow: none;
}
/* invalid color */
.login-main-div .input-field input[type=email].invalid:not(:focus), .login-main-div .input-field input[type=password].invalid:not(:focus) {
  border-bottom: 1px solid #9e9e9e;
  box-shadow: none;
}
/* icon prefix focus color */
.login-main-div .input-field .prefix.active {
  color: rgb(226,0,116);
}

.login-main-div .input-field {
  font-weight: bold;
  color: #fff;
}

.login-main-div .login-error {
    color: #ce2828;
    font-size: 11px;
    border-bottom: 1px solid rgba(99, 10, 10, 0.39);
    font-weight: bold;
    line-height: 2em;
}

</style>
