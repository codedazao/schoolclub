import axios from "axios"
import {ElMessage} from "element-plus";


const authItemName = "access_token"

const defaultFailure = (message, code, url) => {
    //给开发人员看的
    console.error(`请求地址:${url},状态码:${code},控制信息:${message}`)
    ElMessage.warning(message)
}

const defaultError = (err) => {
    console.log(err)
    ElMessage.warning('发生了一些错误，请联系管理员')
}

function takeAccessToken(){
    const authStr = localStorage.getItem(authItemName)||sessionStorage.getItem(authItemName)
    if (authStr === null) return null
    const authObj = JSON.parse(authStr)
    if (authStr.expire <= new Date()){
        deleteAccessToken()
        ElMessage.warning("登录状态已过期，请重新登录")
        return null
    }
    return authObj
}

//删除内存中的token
function deleteAccessToken(){
    localStorage.removeItem(authItemName)
    sessionStorage.removeItem(authItemName)
}

function storeAccessToken(token,expireTime,remember){
    console.log(token,expireTime,remember)
    const authObj = {
        token:token,
        expire:expireTime
    }
    const authStr = JSON.stringify(authObj);
    if (remember)
        localStorage.setItem(authItemName,authStr)
    else
        sessionStorage.setItem(authItemName,authStr)
}


function internalPost(url, data, header, success, failure, error) {
    axios.post(url, data, {
        headers: header
    }).then((data) => {
            // console.log(data)
            if (data.data.code === 200) {
                success(data)
            } else {
                failure(data.data.message, data.data.code, url)
            }
        }
    ).catch((err) => error(err))
}

function internalGet(url, data, header, success, failure, error) {
    axios.get(url, data, {
        headers: header
    }).then((data) => {
            if (data.code === 200) {
                success(data.data)
            } else {
                failure(data.message, data.code, url)
            }
        }
    ).catch((err) => error(err))
}

//登录接口
// @ReturnType{
//     "code": 200,
//     "data": {
//     "expireTime": "2023-09-21 09:34:40.259",
//     "role": "[user]",
//     "token": "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJuYW1lIjoiZGF6YW8iLCJpZCI6MSwiZXhwIjoxNjk1MjYwMDgwLCJqdGkiOiIwMGY0OTg1Mi03ODUyLTQ4MTctYWQ4Yi0zMWU0N2E3ZmZmZWYiLCJhdXRob3JpdGllcyI6WyJ1c2VyIl19.9-p8qwY2uoqPLp0bXfXHJmTIzSIR-P4B1gHmLAU0hyE",
//     "username": "dazao"
// },
//     "message": "登录成功"
// }

function login(username, password, remember, success, failure = defaultFailure) {
    internalPost(
        '/api/auth/login', {
            username: username,
            password: password
        },{
            'Content-Type':'application/x-www-form-urlencoded'
        },
        (data)=>{
            // console.log(data.data)
            // console.log(data.data.data.token,data.data.data.expireTime)
            storeAccessToken(data.data.data.token, data.data.data.expireTime, remember)
            ElMessage.success(`登录成功,欢迎${data.data.data.username}来到我们的系统`)
            success(data)
        }, failure,
        defaultError
    )
}

export {login}