cmake_minimum_required(VERSION 2.8.12)
include(CMakeLists_base.txt)
include_directories(fmu)
add_library(modelfmu SHARED fmu/FMUEnvironment.cpp ${LIB_CONTENT_libsmrt} ${EXEC_CONTENT_main})

# set up the debug executable
# add_executable(fmudebug ${EXEC_CONTENT_main} fmu/fmudebug.cpp)
add_executable(fmudebug ${LIB_CONTENT_libsmrt} ${EXEC_CONTENT_main} fmu/FMUEnvironment.cpp fmu/fmudebug.cpp) # walkaround, the linked library doesn't seem to work
# target_link_libraries(fmudebug libmodelfmu)

if(MSVC)
  set_target_properties(fmudebug PROPERTIES LINK_FLAGS_RELEASE "/LTCG")
else()
	if(!WIN32)
	set_target_properties(fmudebug PROPERTIES LINK_FLAGS_DEBUG "-fsanitize=address")
	endif()
  set_target_properties(fmudebug PROPERTIES LINK_FLAGS_RELEASE "-flto")
endif()
if(Threads_FOUND)
  target_link_libraries(fmudebug "${CMAKE_THREAD_LIBS_INIT}")
endif()
