# For more information about using CMake with Android Studio, read the
# documentation: https://d.android.com/studio/projects/add-native-code.html

cmake_minimum_required(VERSION 3.4.1)
#set(CMAKE_BUILD_TYPE Release FORCE)
add_compile_options(-DLUA_USE_C89)
add_compile_options(-DDLUA_DL_DLOPEN)
add_compile_options(-DLUA_COMPAT_5_1)
add_compile_options(-DLUA_COMPAT_5_2)


# 指定头文件
include_directories(lua)
# 初始化LUA_SRC 变量为lua文件夹的路径
aux_source_directory(lua LUA_SRC)

# 指定源码位置（c or c++）并编译为静态链接库,库名称为lua
add_library(lua STATIC ${LUA_SRC})

# 初始化log-lib 变量为log库
find_library(log-lib log)

target_link_libraries(lua ${log-lib})

#--------------------------------------------------#

# 初始化LUA_JAVA 变量为luajava文件夹路径
aux_source_directory(luajava LUA_JAVA)

# 指定源码位置并编译成动态库；库名为luajava
add_library(luajava SHARED ${LUA_JAVA})



# 为luajava添加依赖：lua以及log
target_link_libraries(luajava lua ${log-lib})