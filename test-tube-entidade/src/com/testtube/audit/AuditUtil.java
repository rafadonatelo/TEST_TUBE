package com.testtube.audit;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Transient;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.lang3.text.WordUtils;

import com.testtube.entidade.EnumLogicoOperacao;
import com.testtube.entidade.IGenericEntity;
import com.testtube.exception.SystemException;

public class AuditUtil {

	/**
	 * verifica os campo
	 */
	public static boolean verificarCampoAtributo(Field field, Object entity) {
		try {

			if (field.isAnnotationPresent(ManyToOne.class) || field.isAnnotationPresent(OneToOne.class)) {
				return true;
			}
			if (field.getType().equals(List.class))
				return false;
			if (field.isAnnotationPresent(Transient.class)) {
				return false;
			}
			try {
				BeanUtils.getSimpleProperty(entity, field.getName());
			} catch (Exception e) {
				return false;
			}

			if ((field.getType().equals(Double.class))) {
				return true;
			} else if ((field.getType().equals(Integer.class))) {
				return true;
			} else if ((field.getType().equals(Date.class))) {
				return true;
			} else if ((field.getType().equals(String.class))) {
				return true;
			} else if ((field.getType().equals(BigDecimal.class))) {
				return true;
			} else if ((field.getType().equals(Boolean.class))) {
				return true;
			} else if ((field.getType().equals(Long.class))) {
				return true;
			} else if (field.getType().isEnum()) {
				return true;
			} else if (field.getType().isPrimitive()) {
				if (field.getName().equalsIgnoreCase("serialVersionUID")) {
					return false;
				}

				if (field.getType().equals(boolean.class)) {
					return false;
				}

				return true;
			}

		} catch (Exception e) {

		}
		return false;
	}

	private static ILogCampoAuditoria gerarLogCampoAuditoria(ILogCampoAuditoria logCampo, Field field, String valorAnterior, String valorAtual, ILogAuditoria logAuditoria) {
		ILogCampoAuditoria logCampoAuditoria = null;

		try {
			logCampoAuditoria = logCampo.getClass().newInstance();

			logCampoAuditoria.setCampo(field.getName());

			logCampoAuditoria.setCampoAnterior(valorAnterior);
			logCampoAuditoria.setCampoAtual(valorAtual);
			logCampoAuditoria.setLogAuditoria(logAuditoria);
		} catch (Exception e) {
			throw new SystemException("Erro ao salvar auditoria.", e);
		}

		return logCampoAuditoria;
	}

	public static void transformarFieldLogCampoAuditoria(IGenericEntity entityPass, Object entity, Field field, ILogCampoAuditoria logCampo, ILogAuditoria logAuditoria,
			List<ILogCampoAuditoria> listLogCampoAuditoria) {
		try {
			String valorAtual = null;
			String valorAnterior = null;
			if (field.isAnnotationPresent(ManyToOne.class) || field.isAnnotationPresent(OneToOne.class)) {
				field.setAccessible(true);
				IGenericEntity objetoAtual = (IGenericEntity) field.get(entity);
				IGenericEntity objetoAnterior = (IGenericEntity) field.get(entityPass);
				if (objetoAtual != null && ((IGenericEntity) objetoAtual).getId() != null) {
					valorAtual = ((IGenericEntity) objetoAtual).getId().toString();
				}
				if (objetoAnterior != null && ((IGenericEntity) objetoAnterior).getId() != null) {
					valorAnterior = ((IGenericEntity) objetoAnterior).getId().toString();
				}
			} else {
				valorAtual = BeanUtils.getSimpleProperty(entity, field.getName());
				valorAnterior = BeanUtils.getSimpleProperty(entityPass, field.getName());

				if (field.getType().equals(BigDecimal.class)) {
					valorAtual = (valorAtual == null) ? null : new BigDecimal(valorAtual).setScale(2).toString();
					valorAnterior = (valorAnterior == null) ? null : new BigDecimal(valorAnterior).setScale(2).toString();
				}

				if (field.getType().equals(Date.class)) {

					Object newValue = null;
					Object oldValue = null;

					try {
						newValue = entity.getClass().getMethod("get" + WordUtils.capitalize(field.getName())).invoke(entity);
					} catch (NoSuchMethodException ex) {
						newValue = entity.getClass().getMethod("is" + WordUtils.capitalize(field.getName())).invoke(entity);
					}

					try {
						oldValue = entityPass.getClass().getMethod("get" + WordUtils.capitalize(field.getName())).invoke(entityPass);
					} catch (NoSuchMethodException ex) {
						oldValue = entityPass.getClass().getMethod("is" + WordUtils.capitalize(field.getName())).invoke(entityPass);
					}

					newValue = (newValue != null) ? String.valueOf(((Date) newValue).getTime()) : "";
					oldValue = (oldValue != null) ? String.valueOf(((Date) oldValue).getTime()) : "";

					if (!newValue.equals("") && !oldValue.equals("")) {
						Date newDate = new Date(new Long(newValue.toString()).longValue());
						Date oldDate = new Date(new Long(oldValue.toString()).longValue());
						SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");

						valorAtual = format.format(newDate);
						valorAnterior = format.format(oldDate);
					}
				}
			}

			if (field.getName().equals("id") || (field.isAnnotationPresent(ManyToOne.class) || field.isAnnotationPresent(OneToOne.class))) {
				ILogCampoAuditoria logCampoAuditoria = gerarLogCampoAuditoria(logCampo, field, valorAnterior, valorAtual, logAuditoria);
				listLogCampoAuditoria.add(logCampoAuditoria);
			} else if ((valorAtual != null && valorAnterior == null) || (valorAtual == null && valorAnterior != null)) {
				ILogCampoAuditoria logCampoAuditoria = gerarLogCampoAuditoria(logCampo, field, valorAnterior, valorAtual, logAuditoria);
				listLogCampoAuditoria.add(logCampoAuditoria);
			} else if (valorAtual != null && valorAnterior != null && !valorAtual.equals(valorAnterior)) {
				ILogCampoAuditoria logCampoAuditoria = gerarLogCampoAuditoria(logCampo, field, valorAnterior, valorAtual, logAuditoria);
				listLogCampoAuditoria.add(logCampoAuditoria);
			}
		} catch (Exception e) {
			throw new SystemException("Erro ao salvar auditoria.", e);
		}
	}

	/**
	 * 
	 * @param entity - Entidade com os dados Atual para gerar Log
	 * @param logAuditoria - entidade de Log
	 * @param logcampoAuditoria - Entidade de Log dos campos da entity
	 * @param parametros - [0]usuario logado, [1]ip da maquina, [2] identificador
	 * @return - O objeto Log para ser persistido * @return - O objeto Log para ser persistido
	 */

	public static ILogAuditoria alterarAuditoria(IGenericEntity entityPass, IGenericEntity entity, ILogAuditoria logAuditoria, ILogCampoAuditoria logCampo, String usuario, String ipMaquinaLocal,
			Map<String, String> parametros) {
		try {
			logAuditoria.setUsuario(usuario);
			logAuditoria.setIpMaquinaLocal(ipMaquinaLocal);
			logAuditoria.setDataAuditoria(new Date());
			logAuditoria.setEntidade(entityPass.getClass().getSimpleName());
			logAuditoria.setOperacao(EnumLogicoOperacao.ALTERACAO);
			Field[] fields = entityPass.getClass().getDeclaredFields();
			List<ILogCampoAuditoria> listLogCampoAuditoria = new ArrayList<ILogCampoAuditoria>();

			for (Field field : fields) {
				if (AuditUtil.verificarCampoAtributo(field, entity)) {
					transformarFieldLogCampoAuditoria(entityPass, entity, field, logCampo, logAuditoria, listLogCampoAuditoria);
				} else if (field.getType().getName().contains("Endereco")) {
					transformarFieldLogCampoAuditoriaEndereco(entityPass, entity, logAuditoria, logCampo, listLogCampoAuditoria);
				}
			}

			// se a lista de campo for maior que 1 - significa que pegou o valor do id, mas as alterações de campos
			// alterados por usuario, se a lista
			// de campo for = 1 - significa que não houve alteração, somente pegou o id.
			if (listLogCampoAuditoria != null && listLogCampoAuditoria.size() > 1) {
				// Itera os parametros de identificadores de referencia ou seja os relacionamento que contem nas
				// Entidades @ManyToOne
				AuditUtil.iteraIdentificadorDeReferencia(logAuditoria, logCampo, listLogCampoAuditoria, parametros);
				logAuditoria.setListLogCampoAuditoria(new ArrayList<ILogCampoAuditoria>(listLogCampoAuditoria));
				return logAuditoria;
			} else {
				return null;
			}
		} catch (Exception e) {
			throw new SystemException("Erro ao salvar auditoria.", e);
		}
	}

	@SuppressWarnings("unused")
	private static void transformarFieldLogCampoAuditoriaEndereco(IGenericEntity entityPass, IGenericEntity entity, ILogAuditoria logAuditoria, ILogCampoAuditoria logCampo,
			List<ILogCampoAuditoria> listLogCampoAuditoria) throws ClassNotFoundException, NoSuchMethodException, IllegalAccessException, InvocationTargetException, NoSuchFieldException {
		Class<?> clsEndVelho = Class.forName(entityPass.getClass().getName());
		Class<?> clsEndNovo = Class.forName(entity.getClass().getName());

		Method methodGetEndVelho = clsEndVelho.getMethod("getEndereco");

		Method methodGetEndNovo = clsEndNovo.getMethod("getEndereco");

		Object enderecoVelho = methodGetEndVelho.invoke(entityPass);
		Object enderecoNovo = methodGetEndVelho.invoke(entity);

		Method methodlistEnderecoNovo[] = enderecoNovo.getClass().getDeclaredMethods();

		for (Method m : methodlistEnderecoNovo) {
			if (m.getName().contains("get")) {
				Object valorNovo = m.invoke(enderecoNovo);

				Method methodGetValorVelho = enderecoVelho.getClass().getMethod(m.getName());

				Object valorVelho = methodGetValorVelho.invoke(enderecoVelho);

				Field campo = enderecoVelho.getClass().getDeclaredField(m.getName().substring(3).toLowerCase());

				if ((valorNovo == null && valorVelho != null) || (valorNovo != null && valorVelho == null)) {
					String valorNovoStr = valorNovo == null ? null : valorNovo.toString();
					String valorVelhoStr = valorVelho == null ? null : valorVelho.toString();

					ILogCampoAuditoria logCampoAuditoria = gerarLogCampoAuditoria(logCampo, campo, valorVelhoStr, valorNovoStr, logAuditoria);

					listLogCampoAuditoria.add(logCampoAuditoria);
				} else if (valorNovo != null && valorVelho != null && !valorNovo.toString().equals(valorVelho.toString())) {
					ILogCampoAuditoria logCampoAuditoria = gerarLogCampoAuditoria(logCampo, campo, valorVelho.toString(), valorNovo.toString(), logAuditoria);

					listLogCampoAuditoria.add(logCampoAuditoria);
				}
			}
		}
	}

	private static void transformarFieldLogCampoAuditoriaEndereco(IGenericEntity entity, ILogAuditoria logAuditoria, ILogCampoAuditoria logCampo, List<ILogCampoAuditoria> listLogCampoAuditoria)
			throws ClassNotFoundException, NoSuchMethodException, IllegalAccessException, InvocationTargetException, NoSuchFieldException {

		Class<?> clsEndNovo = Class.forName(entity.getClass().getName());
		Method methodGetEndNovo = clsEndNovo.getMethod("getEndereco");
		Object enderecoNovo = methodGetEndNovo.invoke(entity);

		Method methodlistEnderecoNovo[] = enderecoNovo.getClass().getDeclaredMethods();

		for (Method m : methodlistEnderecoNovo) {
			if (m.getName().contains("get")) {
				Object valorNovo = m.invoke(enderecoNovo);

				Field campo = enderecoNovo.getClass().getDeclaredField(m.getName().substring(3).toLowerCase());
				if (valorNovo != null) {
					String valorNovoStr = valorNovo == null ? null : valorNovo.toString();
					ILogCampoAuditoria logCampoAuditoria = gerarLogCampoAuditoria(logCampo, campo, null, valorNovoStr, logAuditoria);
					listLogCampoAuditoria.add(logCampoAuditoria);
				}
			}
		}
	}

	/**
	 * 
	 * @param entity - Entidade com os dados Atual para gerar Log
	 * @param logAuditoria - entidade de Log
	 * @param logcampoAuditoria - Entidade de Log dos campos da entity
	 * @param parametros - [0]usuario logado, [1]ip da maquina, [2] identificador
	 * @return - O objeto Log para ser persistido
	 */

	public static ILogAuditoria incluirAuditoria(IGenericEntity entity, ILogAuditoria logAuditoria, ILogCampoAuditoria logCampo, String usuario, String ipMaquinaLocal, Map<String, String> parametros) {
		try {
			logAuditoria.setUsuario(usuario);
			logAuditoria.setIpMaquinaLocal(ipMaquinaLocal);
			logAuditoria.setDataAuditoria(new Date());
			logAuditoria.setEntidade(entity.getClass().getSimpleName());
			logAuditoria.setOperacao(EnumLogicoOperacao.INSERCAO);
			List<ILogCampoAuditoria> listLogCampoAuditoria = null;
			Field[] fields = entity.getClass().getDeclaredFields();

			for (Field field : fields) {
				if (AuditUtil.verificarCampoAtributo(field, entity)) {
					String valorAtual = null;
					/**
					 * Referencia com outros entidades na entidade Pai
					 */
					if (field.isAnnotationPresent(ManyToOne.class) || field.isAnnotationPresent(OneToOne.class)) {
						field.setAccessible(true);
						IGenericEntity objetoAtual = (IGenericEntity) field.get(entity);
						if (objetoAtual != null && ((IGenericEntity) objetoAtual).getId() != null) {
							valorAtual = ((IGenericEntity) objetoAtual).getId().toString();
						}
					} else {
						valorAtual = BeanUtils.getSimpleProperty(entity, field.getName());
					}

					if (valorAtual != null) {

						if (field.getType().equals(Date.class)) {
							Object newValue = null;
							try {
								newValue = entity.getClass().getMethod("get" + WordUtils.capitalize(field.getName())).invoke(entity);
							} catch (NoSuchMethodException ex) {
								newValue = entity.getClass().getMethod("is" + WordUtils.capitalize(field.getName())).invoke(entity);
							}

							newValue = (newValue != null) ? String.valueOf(((Date) newValue).getTime()) : "";

							if (!newValue.equals("")) {
								Date newDate = new Date(new Long(newValue.toString()).longValue());
								SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
								valorAtual = format.format(newDate);
							}
						}

						ILogCampoAuditoria logCampoAuditoria = logCampo.getClass().newInstance();
						logCampoAuditoria.setCampo(field.getName());

						logCampoAuditoria.setCampoAtual(valorAtual);
						logCampoAuditoria.setLogAuditoria(logAuditoria);
						if (listLogCampoAuditoria == null) {
							listLogCampoAuditoria = new ArrayList<ILogCampoAuditoria>();
							listLogCampoAuditoria.add(logCampoAuditoria);
						} else {
							listLogCampoAuditoria.add(logCampoAuditoria);
						}

					}
				} else if (field.getType().getName().contains("Endereco")) {
					transformarFieldLogCampoAuditoriaEndereco(entity, logAuditoria, logCampo, listLogCampoAuditoria);
				}
			}

			// Itera os parametros de identificadores de referencia ou seja os relacionamento que contem nas Entidades
			// @ManyToOne
			AuditUtil.iteraIdentificadorDeReferencia(logAuditoria, logCampo, listLogCampoAuditoria, parametros);

			if (listLogCampoAuditoria != null && listLogCampoAuditoria.size() > 0) {
				logAuditoria.setListLogCampoAuditoria(new ArrayList<ILogCampoAuditoria>(listLogCampoAuditoria));
				return logAuditoria;
			} else {
				return null;
			}
		} catch (Exception e) {
			throw new SystemException("Erro ao salvar auditoria.", e);
		}

	}

	private static void iteraIdentificadorDeReferencia(ILogAuditoria logAuditoria, ILogCampoAuditoria logCampo, List<ILogCampoAuditoria> listLogCampoAuditoria, Map<String, String> parametros)
			throws InstantiationException, IllegalAccessException {
		if (parametros != null && parametros.size() > 0) {
			Set<String> identifadores = parametros.keySet();

			for (String identificador : identifadores) {
				ILogCampoAuditoria logCampoAuditoria = logCampo.getClass().newInstance();
				logCampoAuditoria.setCampo(identificador);

				logCampoAuditoria.setCampoAtual(parametros.get(identificador));
				logCampoAuditoria.setLogAuditoria(logAuditoria);

				if (listLogCampoAuditoria == null) {
					listLogCampoAuditoria = new ArrayList<ILogCampoAuditoria>();
					listLogCampoAuditoria.add(logCampoAuditoria);
				} else {
					listLogCampoAuditoria.add(logCampoAuditoria);
				}
			}
		}
	}

	/**
	 * 
	 * @param entity - Entidade com os dados Atual para gerar Log
	 * @param logAuditoria - entidade de Log
	 * @param logcampoAuditoria - Entidade de Log dos campos da entity
	 * @param parametros - [0]usuario logado, [1]ip da maquina, [2] identificador
	 * @return - O objeto Log para ser persistido
	 */
	public static ILogAuditoria excluirAuditoria(IGenericEntity entity, ILogAuditoria logAuditoria, ILogCampoAuditoria logCampo, String usuario, String ipMaquinaLocal) {
		try {

			logAuditoria.setUsuario(usuario);
			logAuditoria.setIpMaquinaLocal(ipMaquinaLocal);
			logAuditoria.setDataAuditoria(new Date());
			logAuditoria.setEntidade(entity.getClass().getSimpleName());
			logAuditoria.setOperacao(EnumLogicoOperacao.REMOCAO);
			List<ILogCampoAuditoria> listLogCampoAuditoria = null;
			Field[] fields = entity.getClass().getDeclaredFields();

			for (Field field : fields) {
				if (AuditUtil.verificarCampoAtributo(field, entity)) {
					String valorAtual = null;

					/**
					 * Referencia com outros entidades na entidade Pai
					 */
					if (field.isAnnotationPresent(ManyToOne.class) || field.isAnnotationPresent(OneToOne.class)) {
						field.setAccessible(true);
						IGenericEntity objetoAtual = (IGenericEntity) field.get(entity);
						if (objetoAtual != null && ((IGenericEntity) objetoAtual).getId() != null) {
							valorAtual = ((IGenericEntity) objetoAtual).getId().toString();
						}
					} else {
						valorAtual = BeanUtils.getSimpleProperty(entity, field.getName());
					}

					if (valorAtual != null) {

						if (field.getType().equals(Date.class)) {
							Object newValue = null;
							try {
								newValue = entity.getClass().getMethod("get" + WordUtils.capitalize(field.getName())).invoke(entity);
							} catch (NoSuchMethodException ex) {
								newValue = entity.getClass().getMethod("is" + WordUtils.capitalize(field.getName())).invoke(entity);
							}

							newValue = (newValue != null) ? String.valueOf(((Date) newValue).getTime()) : "";

							if (!newValue.equals("")) {
								Date newDate = new Date(new Long(newValue.toString()).longValue());
								SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
								valorAtual = format.format(newDate);
							}
						}

						ILogCampoAuditoria logCampoAuditoria = logCampo.getClass().newInstance();
						logCampoAuditoria.setCampo(field.getName());
						logCampoAuditoria.setCampoAtual(valorAtual);
						logCampoAuditoria.setLogAuditoria(logAuditoria);

						if (listLogCampoAuditoria == null) {
							listLogCampoAuditoria = new ArrayList<ILogCampoAuditoria>();
							listLogCampoAuditoria.add(logCampoAuditoria);
						} else {
							listLogCampoAuditoria.add(logCampoAuditoria);
						}
					}
				}
			}

			if (listLogCampoAuditoria != null && listLogCampoAuditoria.size() > 0) {
				logAuditoria.setListLogCampoAuditoria(new ArrayList<ILogCampoAuditoria>(listLogCampoAuditoria));

				return logAuditoria;
			} else {
				return null;
			}
		} catch (Exception e) {
			throw new SystemException("Erro ao salvar auditoria.", e);
		}

	}

	/**
	 * 
	 * @param entity - Entidade com os dados Atual para gerar Log
	 * @param entityPass - Entidade com os dados antigos para gerar log
	 * @param logAuditoria - entidade de Log
	 * @param logcampoAuditoria - Entidade de Log dos campos da entity
	 * @param usuario - usuario logado
	 * @param ipMaquina - ip da maquina
	 * @return - O objeto Log para ser persistido
	 */
	public static ILogAuditoria auditEntity(IGenericEntity entity, IGenericEntity entityPass, ILogAuditoria logAuditoria, ILogCampoAuditoria logcampoAuditoria, String usuario, String ipMaquinaLocal,
			Map<String, String> parametros) {
		try {
			if (entityPass == null) {
				return incluirAuditoria(entity, logAuditoria, logcampoAuditoria, usuario, ipMaquinaLocal, parametros);
			} else {
				return alterarAuditoria(entityPass, entity, logAuditoria, logcampoAuditoria, usuario, ipMaquinaLocal, parametros);
			}
		} catch (Exception e) {
			throw new SystemException("Erro ao salvar auditoria.", e);
		}
	}

	/**
	 * 
	 * @param entity - Entidade com os dados Atual para gerar Log
	 * @param logAuditoria - entidade de Log
	 * @param logcampoAuditoria - Entidade de Log dos campos da entity
	 * @param usuario - usuario logado
	 * @param ipMaquina - ip da maquina logada no sistema
	 * @return
	 */
	@SuppressWarnings("static-access")
	public ILogAuditoria auditEntity(IGenericEntity entity, ILogAuditoria logAuditoria, ILogCampoAuditoria logcampoAuditoria, String usuario, String ipMaquinaLocal, Map<String, String> parametros) {
		try {
			return this.incluirAuditoria(entity, logAuditoria, logcampoAuditoria, usuario, ipMaquinaLocal, parametros);
		} catch (Exception e) {
			throw new SystemException("Erro ao salvar auditoria.", e);
		}
	}

}
