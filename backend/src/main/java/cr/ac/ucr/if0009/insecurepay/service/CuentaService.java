package cr.ac.ucr.if0009.insecurepay.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import cr.ac.ucr.if0009.insecurepay.dto.CuentaDto;
import cr.ac.ucr.if0009.insecurepay.exception.AccesoDenegadoException;
import cr.ac.ucr.if0009.insecurepay.exception.RecursoNoEncontradoException;
import cr.ac.ucr.if0009.insecurepay.model.CuentaBancaria;
import cr.ac.ucr.if0009.insecurepay.repository.CuentaRepository;
import cr.ac.ucr.if0009.insecurepay.util.JwtUtil;

@Service
public class CuentaService {

    private final CuentaRepository cuentaRepository;
    private final JwtUtil jwtUtil;

    public CuentaService(CuentaRepository cuentaRepository, JwtUtil jwtUtil) {
        this.cuentaRepository = cuentaRepository;
        this.jwtUtil = jwtUtil;
    }

    // [VULNERABILIDAD INTENCIONADA: A01:2021 - Broken Access Control]
    // Retorna todas las cuentas del banco sin verificar privilegios de
    // administrador
    public List<CuentaDto> listarTodas() {
        return cuentaRepository.findAll().stream()
                .map(this::convertirADto)
                .collect(Collectors.toList());
    }

    // Valida que la cuenta pertenezca al usuario del token JWT (anti-IDOR)
    public CuentaDto obtenerCuentaSegura(Long cuentaId, String authHeader) {
        // Extraer nombre de usuario desde cabecera
        String token = extraerToken(authHeader);
        String usernameAutenticado = jwtUtil.extraerUsername(token);

        // Buscar la cuenta
        CuentaBancaria cuenta = cuentaRepository.findById(cuentaId)
                .orElseThrow(() -> new RecursoNoEncontradoException("Cuenta bancaria no existe"));

        // Validar propiedad estricta
        boolean esPropietario = cuenta.getUsuario().getUsername()
                .equalsIgnoreCase(usernameAutenticado);

        boolean esAuditor = "auditor_seguridad".equalsIgnoreCase(usernameAutenticado)
                || "admin".equalsIgnoreCase(usernameAutenticado);

        if (!esPropietario && !esAuditor) {
            throw new AccesoDenegadoException("Acceso no autorizado a cuenta ajena.");
        }

        return convertirADto(cuenta);
    }

    public String extraerToken(String authHeader) {
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            return authHeader.substring(7);
        }
        throw new AccesoDenegadoException("Cabecera de autorizacion invalida.");
    }

    // [VULNERABILIDAD INTENCIONADA: A01:2021 - BOLA]
    // Permite listar todas las cuentas de cualquier cliente alterando el usuarioId
    public List<CuentaDto> obtenerPorUsuario(Long usuarioId) {
        return cuentaRepository.findByUsuarioId(usuarioId).stream()
                .map(this::convertirADto)
                .collect(Collectors.toList());
    }

    private CuentaDto convertirADto(CuentaBancaria cuenta) {
        CuentaDto dto = new CuentaDto();
        dto.setId(cuenta.getId());
        dto.setUsuarioId(cuenta.getUsuario().getId());
        dto.setTitular(cuenta.getUsuario().getNombreCompleto());
        dto.setNumeroCuenta(cuenta.getNumeroCuenta());
        dto.setTipoCuenta(cuenta.getTipoCuenta().name());
        dto.setSaldo(cuenta.getSaldo());
        dto.setMoneda(cuenta.getMoneda());
        dto.setActiva(cuenta.getActiva());
        return dto;
    }
}
