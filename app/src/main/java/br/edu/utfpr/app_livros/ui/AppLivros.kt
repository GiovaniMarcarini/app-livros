package br.edu.utfpr.app_livros.ui

import UsuariosListScreen
import androidx.compose.material3.DrawerState
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import br.edu.utfpr.app_livros.ui.usuario.form.UsuarioFormScreen
import br.edu.utfpr.app_livros.ui.utils.composables.AppModalDrawer
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

private object Screens {
    const val LIST_USUARIOS = "listUsuarios"
    const val USUARIO_FORM = "usuarioForm"
    const val USUARIO_DETAILS = "usuarioDetails"
    const val LOGIN = "login"
}

object Arguments {
    const val ID = "id"
}

private object Routes {
    const val LIST_USUARIOS = Screens.LIST_USUARIOS
    const val USUARIO_FORM = "${Screens.USUARIO_FORM}?${Arguments.ID}={${Arguments.ID}}"
    const val USUARIO_DETAILS = "${Screens.USUARIO_DETAILS}/{${Arguments.ID}}"
    const val LOGIN = Screens.LOGIN
}

@Composable
fun AppLivros(
    modifier: Modifier = Modifier,
    navController: NavHostController = rememberNavController(),
    drawerState: DrawerState = rememberDrawerState(initialValue = DrawerValue.Closed),
    coroutineScope: CoroutineScope = rememberCoroutineScope(),
    startRoute: String = Routes.LOGIN
) {
    val currentNavBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = currentNavBackStackEntry?.destination?.route ?: startRoute

    NavHost(
        navController = navController,
        startDestination = startRoute,
        modifier = modifier
    ) {
        composable(route = Routes.LOGIN) {
            LoginScreen(
                onLoginSuccess = {
                    navController.navigate(Routes.LIST_USUARIOS) {
                        popUpTo(navController.graph.findStartDestination().id) {
                            inclusive = true
                        }
                    }
                }
            )
        }
        composable(route = Routes.LIST_USUARIOS) {
            AppModalDrawer(
                drawerState = drawerState,
                currentRoute = currentRoute,
                onUsuariosPressed = { navigateToListUsuarios(navController) }
            ) {
                UsuariosListScreen(
                    onUsuarioPressed = { usuario ->
                        navController.navigate("${Screens.USUARIO_DETAILS}/${usuario.id}")
                    },
                    onAddPressed = {
                        navController.navigate(Screens.USUARIO_FORM)
                    },
                    openDrawer = {
                        coroutineScope.launch { drawerState.open() }
                    }
                )
            }
        }
        composable(
            route = Routes.USUARIO_FORM,
            arguments = listOf(
                navArgument(name = Arguments.ID) { type = NavType.StringType; nullable = true}
            )
        ) {
            UsuarioFormScreen(
                onBackPressed = {
                    navController.popBackStack()
                },
                onusuarioSaved = {
                    navigateToListUsuarios(navController)
                }
            )
        }
    }
}

private fun navigateToListUsuarios(navController: NavHostController) {
    navController.navigate(Routes.LIST_USUARIOS) {
        popUpTo(navController.graph.findStartDestination().id) {
            inclusive = true
        }
    }
}

