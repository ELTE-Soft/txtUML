cmake_minimum_required(VERSION 2.8.12)
include(CMakeLists_base.txt)
include_directories(fmu)
add_library(libmodelfmu fmu/FMUEnvironment.cpp ${LIB_CONTENT_libsmrt} ${EXEC_CONTENT_main})